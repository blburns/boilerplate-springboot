#!/usr/bin/env bash
# manage.sh — project control plane for boilerplate-springboot
# Assumes PostgreSQL is already installed and reachable (default: localhost:5432).
# Docker Compose remains optional appendix only — not required for day-to-day use.
#
# Usage: ./scripts/manage.sh <command> [args...]
#        make <target>
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

PROFILE="${SPRING_PROFILES_ACTIVE:-local}"
GRADLEW=(./gradlew --no-daemon)

DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-boilerplate}"
DB_USER="${DB_USER:-boilerplate}"
DB_PASSWORD="${DB_PASSWORD:-boilerplate}"

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

die() {
  echo "error: $*" >&2
  exit 1
}

info() {
  echo "==> $*"
}

ensure_java() {
  local candidate=""
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    if "${JAVA_HOME}/bin/java" -version 2>&1 | grep -Eq 'version "21\.'; then
      export PATH="$JAVA_HOME/bin:$PATH"
      return 0
    fi
  fi
  if [[ -x /usr/libexec/java_home ]]; then
    candidate="$(/usr/libexec/java_home -v 21 2>/dev/null || true)"
    if [[ -n "$candidate" ]]; then
      export JAVA_HOME="$candidate"
      export PATH="$JAVA_HOME/bin:$PATH"
      return 0
    fi
  fi
  if command -v java >/dev/null 2>&1 && java -version 2>&1 | grep -Eq 'version "21\.'; then
    return 0
  fi
  die "JDK 21 required. Install Temurin/JBR 21 and set JAVA_HOME, or run: export JAVA_HOME=\$(/usr/libexec/java_home -v 21)"
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || die "'$1' not found on PATH"
}

gradle() {
  ensure_java
  "${GRADLEW[@]}" "$@"
}

psql_admin() {
  # Prefer connecting as local peer/superuser to create roles/dbs
  if command -v psql >/dev/null 2>&1; then
    if [[ -n "${PGUSER:-}" ]]; then
      psql "$@"
    elif psql -d postgres -c 'SELECT 1' >/dev/null 2>&1; then
      psql -d postgres "$@"
    elif psql -U postgres -d postgres -c 'SELECT 1' >/dev/null 2>&1; then
      psql -U postgres -d postgres "$@"
    else
      die "cannot connect to Postgres as a superuser. Set PGUSER or run as a role that can CREATE ROLE/DATABASE."
    fi
  else
    die "psql not found. Install PostgreSQL client tools."
  fi
}

usage() {
  cat <<'EOF'
boilerplate-springboot manage

Usage:
  ./scripts/manage.sh <command> [args...]
  make <command>

Lifecycle
  run                 Start app (bootRun, profile=local) against existing Postgres
  jar                 Build fat JAR (:app:bootJar)
  build               Compile + bootJar
  clean               ./gradlew clean

Quality
  test                Run all tests (integration tests need reachable Postgres)
  format | fmt        Spotless apply
  check | lint        Spotless check
  verify              spotlessCheck + test + bootJar (CI-like)

Postgres (local / already installed)
  db-check            Verify DB_HOST/DB_PORT/DB_NAME credentials
  db-init             Create role + database (idempotent; needs superuser via psql)

Ops
  status              Show Java, Postgres reachability, /actuator/health
  stop-daemon         ./gradlew --stop
  toolchains          List detected JDKs

Optional Docker appendix (not required)
  compose-up          docker compose up --build
  compose-down        docker compose down
  compose-down-v      docker compose down -v (destructive)
  docker-build        Build app image only

Help
  help                Show this message

Environment (defaults)
  SPRING_PROFILES_ACTIVE=local
  DB_HOST=localhost  DB_PORT=5432  DB_NAME=boilerplate
  DB_USER=boilerplate  DB_PASSWORD=boilerplate
  JAVA_HOME           prefer JDK 21
  SERVER_PORT         default 8080
EOF
}

# ---------------------------------------------------------------------------
# Commands
# ---------------------------------------------------------------------------

cmd_run() {
  for arg in "$@"; do
    case "$arg" in
      -h|--help)
        echo "Usage: manage.sh run"
        echo "Requires Postgres already running (see db-check / db-init)."
        return 0
        ;;
      --with-db)
        die "--with-db was removed. Postgres must already be installed. Use: manage.sh db-init && manage.sh db-check && manage.sh run"
        ;;
      *) die "unknown option for run: $arg" ;;
    esac
  done

  ensure_java
  cmd_db_check
  info "JAVA_HOME=${JAVA_HOME:-"(from PATH)"}"
  info "profile=${PROFILE}"
  info "datasource=${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME}"
  info "starting :app:bootRun — http://localhost:${SERVER_PORT:-8080}/items"
  ./gradlew :app:bootRun --args="--spring.profiles.active=${PROFILE}"
}

cmd_jar() {
  info "building fat JAR"
  gradle :app:bootJar -x test
  ls -lh app/build/libs/*.jar 2>/dev/null || true
}

cmd_build() {
  info "compiling and packaging"
  gradle classes :app:bootJar -x test
}

cmd_clean() {
  info "cleaning build outputs"
  gradle clean
}

cmd_test() {
  info "running tests (integration tests use DB_* against Postgres)"
  export DB_HOST DB_PORT DB_NAME DB_USER DB_PASSWORD
  gradle test
}

cmd_format() {
  info "applying Spotless"
  gradle spotlessApply
}

cmd_check() {
  info "running Spotless check"
  gradle spotlessCheck
}

cmd_verify() {
  info "CI-like verify: spotlessCheck + test + bootJar"
  export DB_HOST DB_PORT DB_NAME DB_USER DB_PASSWORD
  gradle spotlessCheck test :app:bootJar
}

cmd_db_check() {
  require_cmd psql
  info "checking Postgres ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME}"
  if PGPASSWORD="$DB_PASSWORD" psql \
    -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" \
    -c 'SELECT 1' >/dev/null 2>&1; then
    info "Postgres OK"
  else
    die "cannot connect to ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME}. Create it with: ./scripts/manage.sh db-init"
  fi
}

cmd_db_init() {
  require_cmd psql
  info "ensuring role/database ${DB_NAME} (user ${DB_USER})"
  local sql_file="$ROOT/scripts/sql/init_local_db.sql"
  [[ -f "$sql_file" ]] || die "missing $sql_file"

  # Run as admin against the maintenance DB; script is idempotent.
  if [[ -n "${PGUSER:-}" ]]; then
    psql -v ON_ERROR_STOP=1 -f "$sql_file"
  elif psql -d postgres -v ON_ERROR_STOP=1 -f "$sql_file" >/dev/null 2>&1; then
    true
  elif psql -U postgres -d postgres -v ON_ERROR_STOP=1 -f "$sql_file"; then
    true
  else
    die "db-init failed. Run as a Postgres superuser, e.g. PGUSER=postgres ./scripts/manage.sh db-init"
  fi
  cmd_db_check
}

cmd_status() {
  echo "--- Java ---"
  ensure_java || true
  if [[ -n "${JAVA_HOME:-}" ]]; then
    echo "JAVA_HOME=$JAVA_HOME"
  fi
  if command -v java >/dev/null 2>&1; then
    java -version 2>&1 || true
  fi
  echo
  echo "--- Gradle ---"
  if [[ -x ./gradlew ]]; then
    ensure_java
    ./gradlew --version 2>/dev/null | head -5 || true
  fi
  echo
  echo "--- Postgres ---"
  echo "target: ${DB_USER}@${DB_HOST}:${DB_PORT}/${DB_NAME}"
  if command -v psql >/dev/null 2>&1 \
    && PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c 'SELECT 1' >/dev/null 2>&1; then
    echo "status: reachable"
  else
    echo "status: unreachable (run: manage.sh db-init && manage.sh db-check)"
  fi
  echo
  echo "--- Health ---"
  local port="${SERVER_PORT:-8080}"
  if curl -sf "http://localhost:${port}/actuator/health" >/dev/null 2>&1; then
    curl -s "http://localhost:${port}/actuator/health"
    echo
  else
    echo "app not responding on :${port}"
  fi
}

cmd_stop_daemon() {
  ensure_java
  info "stopping Gradle daemons"
  ./gradlew --stop
}

cmd_toolchains() {
  gradle -q javaToolchains
}

cmd_compose_up() {
  require_cmd docker
  info "optional: docker compose up --build"
  docker compose up --build "$@"
}

cmd_compose_down() {
  require_cmd docker
  docker compose down "$@"
}

cmd_compose_down_v() {
  require_cmd docker
  info "optional: docker compose down -v (destructive)"
  docker compose down -v
}

cmd_docker_build() {
  require_cmd docker
  info "optional: building docker image"
  docker build -f docker/Dockerfile -t boilerplate-springboot:local .
}

# ---------------------------------------------------------------------------
# Dispatch
# ---------------------------------------------------------------------------

main() {
  local cmd="${1:-help}"
  shift || true

  case "$cmd" in
    help|-h|--help) usage ;;
    run)            cmd_run "$@" ;;
    jar|package)    cmd_jar "$@" ;;
    build)          cmd_build "$@" ;;
    clean)          cmd_clean "$@" ;;
    test)           cmd_test "$@" ;;
    format|fmt)     cmd_format "$@" ;;
    check|lint)     cmd_check "$@" ;;
    verify)         cmd_verify "$@" ;;
    db-check)       cmd_db_check "$@" ;;
    db-init)        cmd_db_init "$@" ;;
    # Removed Docker-centric aliases — point people at local Postgres
    db-up|db-down|db-reset|run-db)
      die "'$cmd' removed. Use local Postgres: db-init / db-check / run. Optional Compose: compose-up / compose-down."
      ;;
    up)             cmd_compose_up "$@" ;;
    down)           cmd_compose_down "$@" ;;
    down-v|compose-down-v) cmd_compose_down_v "$@" ;;
    compose-up)     cmd_compose_up "$@" ;;
    compose-down)   cmd_compose_down "$@" ;;
    docker-build)   cmd_docker_build "$@" ;;
    status)         cmd_status "$@" ;;
    stop-daemon)    cmd_stop_daemon "$@" ;;
    toolchains)     cmd_toolchains "$@" ;;
    *)
      echo "unknown command: $cmd" >&2
      echo >&2
      usage >&2
      exit 1
      ;;
  esac
}

main "$@"

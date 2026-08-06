#!/usr/bin/env bash
# manage.sh — project control plane for boilerplate-springboot
# Usage: ./scripts/manage.sh <command> [args...]
#        make <target>
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

PROFILE="${SPRING_PROFILES_ACTIVE:-local}"
GRADLEW=(./gradlew --no-daemon)

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

docker_compose() {
  require_cmd docker
  docker compose "$@"
}

usage() {
  cat <<'EOF'
boilerplate-springboot manage

Usage:
  ./scripts/manage.sh <command> [args...]
  make <command>

Lifecycle
  run [--with-db]     Start app (bootRun, profile=local). Optional: start Postgres first
  jar                 Build fat JAR (:app:bootJar)
  build               Format check skipped; compile + bootJar
  clean               ./gradlew clean

Quality
  test                Run all tests (Testcontainers needs Docker)
  format | fmt        Spotless apply
  check | lint        Spotless check
  verify              format check + test + bootJar (CI-like)

Data / Docker
  db-up               Start Postgres via Compose
  db-down             Stop Postgres
  db-reset            Stop Postgres and delete volume (destructive)
  up                  docker compose up --build (app + Postgres)
  down                docker compose down
  down-v              docker compose down -v (destructive)
  docker-build        Build app image only

Ops
  status              Show Java, Docker, Compose, /actuator/health
  stop-daemon         ./gradlew --stop
  toolchains          List detected JDKs
  logs [service]      Compose logs (default: app)
  ps                  Compose process status

Help
  help                Show this message

Environment
  SPRING_PROFILES_ACTIVE   default: local (for run)
  JAVA_HOME                prefer JDK 21
  SERVER_PORT              default 8080 (health checks)
EOF
}

# ---------------------------------------------------------------------------
# Commands
# ---------------------------------------------------------------------------

cmd_run() {
  local with_db=0
  for arg in "$@"; do
    case "$arg" in
      --with-db) with_db=1 ;;
      -h|--help)
        echo "Usage: manage.sh run [--with-db]"
        return 0
        ;;
      *) die "unknown option for run: $arg" ;;
    esac
  done

  if [[ "$with_db" -eq 1 ]]; then
    cmd_db_up
  fi

  ensure_java
  info "JAVA_HOME=${JAVA_HOME:-"(from PATH)"}"
  info "profile=${PROFILE}"
  info "starting :app:bootRun — http://localhost:${SERVER_PORT:-8080}/items"
  # Allow Gradle daemon for interactive run (faster restarts)
  ensure_java
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
  info "running tests"
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
  gradle spotlessCheck test :app:bootJar
}

cmd_db_up() {
  info "starting Postgres"
  docker_compose up -d postgres
  info "waiting for healthy Postgres…"
  local i=0
  until docker_compose exec -T postgres pg_isready -U boilerplate -d boilerplate >/dev/null 2>&1; do
    i=$((i + 1))
    if [[ $i -gt 30 ]]; then
      die "Postgres did not become ready"
    fi
    sleep 1
  done
  info "Postgres is ready on localhost:5432"
}

cmd_db_down() {
  info "stopping Postgres"
  docker_compose stop postgres
}

cmd_db_reset() {
  info "resetting Postgres volume (destructive)"
  docker_compose down -v --remove-orphans
  cmd_db_up
}

cmd_up() {
  info "compose up --build"
  docker_compose up --build "$@"
}

cmd_down() {
  docker_compose down "$@"
}

cmd_down_v() {
  info "compose down -v (destructive)"
  docker_compose down -v
}

cmd_docker_build() {
  require_cmd docker
  info "building docker image"
  docker build -f docker/Dockerfile -t boilerplate-springboot:local .
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
  echo "--- Docker ---"
  if command -v docker >/dev/null 2>&1; then
    docker version --format 'Client: {{.Client.Version}}' 2>/dev/null || docker --version
    echo
    docker_compose ps 2>/dev/null || echo "(compose project not running)"
  else
    echo "docker not installed"
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

cmd_logs() {
  local service="${1:-app}"
  docker_compose logs -f "$service"
}

cmd_ps() {
  docker_compose ps
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
    db-up)          cmd_db_up "$@" ;;
    db-down)        cmd_db_down "$@" ;;
    db-reset)       cmd_db_reset "$@" ;;
    up)             cmd_up "$@" ;;
    down)           cmd_down "$@" ;;
    down-v)         cmd_down_v "$@" ;;
    docker-build)   cmd_docker_build "$@" ;;
    status)         cmd_status "$@" ;;
    stop-daemon)    cmd_stop_daemon "$@" ;;
    toolchains)     cmd_toolchains "$@" ;;
    logs)           cmd_logs "$@" ;;
    ps)             cmd_ps "$@" ;;
    *)
      echo "unknown command: $cmd" >&2
      echo >&2
      usage >&2
      exit 1
      ;;
  esac
}

main "$@"

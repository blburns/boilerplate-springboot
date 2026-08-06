#!/usr/bin/env bash
# Run the app on the local profile, forcing a JDK 21 Gradle daemon when possible.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if [[ -z "${JAVA_HOME:-}" ]] || ! "$JAVA_HOME/bin/java" -version 2>&1 | grep -q 'version "21\.'; then
  if [[ -x /usr/libexec/java_home ]]; then
    if CANDIDATE="$(/usr/libexec/java_home -v 21 2>/dev/null)"; then
      export JAVA_HOME="$CANDIDATE"
    fi
  fi
fi

if [[ -z "${JAVA_HOME:-}" ]]; then
  echo "JAVA_HOME must point at JDK 21. Install Temurin 21 or JetBrains Runtime 21." >&2
  exit 1
fi

echo "Using JAVA_HOME=$JAVA_HOME"
"$JAVA_HOME/bin/java" -version
./gradlew --stop >/dev/null
exec ./gradlew :app:bootRun --args='--spring.profiles.active=local' "$@"

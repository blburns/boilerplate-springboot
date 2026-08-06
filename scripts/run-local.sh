#!/usr/bin/env bash
# Thin alias — prefer ./scripts/manage.sh run or: make run
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
exec "$ROOT/scripts/manage.sh" run "$@"

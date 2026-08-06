# Makefile — thin wrappers around ./scripts/manage.sh
# Prefer: make help
# Postgres must already be installed/running (see make db-init).

.PHONY: help run jar build clean test format fmt check lint verify \
	db-check db-init \
	compose-up compose-down compose-down-v docker-build \
	status stop-daemon toolchains

MANAGE := ./scripts/manage.sh

help:
	@$(MANAGE) help

run:
	@$(MANAGE) run $(ARGS)

jar package:
	@$(MANAGE) jar

build:
	@$(MANAGE) build

clean:
	@$(MANAGE) clean

test:
	@$(MANAGE) test

format fmt:
	@$(MANAGE) format

check lint:
	@$(MANAGE) check

verify:
	@$(MANAGE) verify

db-check:
	@$(MANAGE) db-check

db-init:
	@$(MANAGE) db-init

status:
	@$(MANAGE) status

stop-daemon:
	@$(MANAGE) stop-daemon

toolchains:
	@$(MANAGE) toolchains

# Optional Docker appendix (not part of the default workflow)
compose-up:
	@$(MANAGE) compose-up

compose-down:
	@$(MANAGE) compose-down

compose-down-v:
	@$(MANAGE) compose-down-v

docker-build:
	@$(MANAGE) docker-build

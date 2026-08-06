# Makefile — thin wrappers around ./scripts/manage.sh
# Prefer: make help

.PHONY: help run jar build clean test format fmt check lint verify \
	db-up db-down db-reset up down down-v docker-build \
	status stop-daemon toolchains logs ps

MANAGE := ./scripts/manage.sh

help:
	@$(MANAGE) help

run:
	@$(MANAGE) run $(ARGS)

run-db:
	@$(MANAGE) run --with-db

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

db-up:
	@$(MANAGE) db-up

db-down:
	@$(MANAGE) db-down

db-reset:
	@$(MANAGE) db-reset

up:
	@$(MANAGE) up

down:
	@$(MANAGE) down

down-v:
	@$(MANAGE) down-v

docker-build:
	@$(MANAGE) docker-build

status:
	@$(MANAGE) status

stop-daemon:
	@$(MANAGE) stop-daemon

toolchains:
	@$(MANAGE) toolchains

logs:
	@$(MANAGE) logs $(SERVICE)

ps:
	@$(MANAGE) ps

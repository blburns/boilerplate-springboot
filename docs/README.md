# Documentation

Comprehensive documentation for the Spring Boot multi-module boilerplate.

## Start Here

| Audience | First read |
|----------|------------|
| **End user** | [User Guide](02-user-guide/README.md) → [Using the Web UI](02-user-guide/USING_THE_WEB_UI.md) |
| **New install** | [Getting Started](01-getting-started/GETTING_STARTED.md) |
| **Administrator** | [Admin Guide](05-operations/ADMIN_GUIDE.md) |
| **Developer** | [Architecture](03-architecture/ARCHITECTURE.md) → [Adding a Feature](04-development/ADDING_A_FEATURE.md) |
| **Overview** | [00-OVERVIEW.md](00-OVERVIEW.md) |

Full map: [DOCUMENTATION_MAP.md](DOCUMENTATION_MAP.md)

---

## Getting Started

**For:** First-time setup

| Doc | Description |
|-----|-------------|
| [GETTING_STARTED](01-getting-started/GETTING_STARTED.md) | Prerequisites, install, run locally or with Compose |
| [FIRST_RUN_CHECKLIST](01-getting-started/FIRST_RUN_CHECKLIST.md) | Post-start verification |

---

## User Guide

**For:** People exercising the sample Items UI and API

| Doc | Description |
|-----|-------------|
| [README](02-user-guide/README.md) | User guide index |
| [CONCEPTS](02-user-guide/CONCEPTS.md) | What this boilerplate demonstrates |
| [USING_THE_WEB_UI](02-user-guide/USING_THE_WEB_UI.md) | Create, list, and delete items in the browser |
| [USING_THE_API](02-user-guide/USING_THE_API.md) | Call the REST API (curl / Swagger) |

---

## Architecture

**For:** Developers and technical leads

| Doc | Description |
|-----|-------------|
| [ARCHITECTURE](03-architecture/ARCHITECTURE.md) | Layering, request flow, tech stack |
| [MODULES](03-architecture/MODULES.md) | Module responsibilities and dependency rules |

---

## Development

**For:** Engineers extending the boilerplate

| Doc | Description |
|-----|-------------|
| [README](04-development/README.md) | Development index |
| [ADDING_A_FEATURE](04-development/ADDING_A_FEATURE.md) | Step-by-step feature recipe |
| [API_REFERENCE](04-development/API_REFERENCE.md) | Items REST contract and errors |
| [TESTING](04-development/TESTING.md) | Unit + host-Postgres integration tests |
| [CODING_CONVENTIONS](04-development/CODING_CONVENTIONS.md) | Packages, formatting, security notes |
| [RBAC_AND_ADMIN](04-development/RBAC_AND_ADMIN.md) | Admin shell, form login, permissions |

---

## Operations

**For:** Administrators and DevOps

| Doc | Description |
|-----|-------------|
| [ADMIN_GUIDE](05-operations/ADMIN_GUIDE.md) | Day-2 operations overview |
| [CONFIGURATION](05-operations/CONFIGURATION.md) | Profiles and environment variables |
| [DEPLOYMENT](05-operations/DEPLOYMENT.md) | JAR, Compose, and GHCR image |
| [MONITORING](05-operations/MONITORING.md) | Health, Actuator, Prometheus |
| [TROUBLESHOOTING](05-operations/TROUBLESHOOTING.md) | Common failures and fixes |

---

## Reference

**For:** Quick lookup

| Doc | Description |
|-----|-------------|
| [ENVIRONMENT_VARIABLES](06-reference/ENVIRONMENT_VARIABLES.md) | Full env var table |
| [DATABASE_SCHEMA](06-reference/DATABASE_SCHEMA.md) | Tables, migrations, indexes |
| [GRADLE_TASKS](06-reference/GRADLE_TASKS.md) | Common Gradle commands |

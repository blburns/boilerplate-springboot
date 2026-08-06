# Documentation Map

Quick routing by task.

## I want to…

| Task | Go to |
|------|-------|
| Install and run locally | [GETTING_STARTED](01-getting-started/GETTING_STARTED.md) |
| Confirm the app is healthy after first start | [FIRST_RUN_CHECKLIST](01-getting-started/FIRST_RUN_CHECKLIST.md) |
| Create an item in the browser | [USING_THE_WEB_UI](02-user-guide/USING_THE_WEB_UI.md) |
| Call the API with curl / Swagger | [USING_THE_API](02-user-guide/USING_THE_API.md) |
| Understand module layout | [MODULES](03-architecture/MODULES.md) |
| Add a new business feature | [ADDING_A_FEATURE](04-development/ADDING_A_FEATURE.md) |
| See exact REST endpoints | [API_REFERENCE](04-development/API_REFERENCE.md) |
| Write / run tests | [TESTING](04-development/TESTING.md) |
| Change DB credentials or ports | [CONFIGURATION](05-operations/CONFIGURATION.md) |
| Deploy with Docker / GHCR | [DEPLOYMENT](05-operations/DEPLOYMENT.md) |
| Wire Prometheus or check health | [MONITORING](05-operations/MONITORING.md) |
| Fix “connection refused” / Flyway errors | [TROUBLESHOOTING](05-operations/TROUBLESHOOTING.md) |
| Look up env vars or schema | [06-reference](06-reference/ENVIRONMENT_VARIABLES.md) |

## Audience paths

```text
End user ──► 02-user-guide
Installer ─► 01-getting-started
Developer ─► 03-architecture + 04-development
Admin ─────► 05-operations + 06-reference
```

-- Idempotent local Postgres bootstrap for boilerplate-springboot.
-- Run as a superuser (e.g. peer auth as your OS user, or -U postgres).
-- Defaults match application-local.yml.

DO
$$
BEGIN
  IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'boilerplate') THEN
    CREATE ROLE boilerplate LOGIN PASSWORD 'boilerplate';
  END IF;
END
$$;

SELECT 'CREATE DATABASE boilerplate OWNER boilerplate'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'boilerplate')\gexec

GRANT ALL PRIVILEGES ON DATABASE boilerplate TO boilerplate;

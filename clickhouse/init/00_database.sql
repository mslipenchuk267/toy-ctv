-- Allow MaterializedPostgreSQL (DB engine) for this session
SET allow_experimental_database_materialized_postgresql = 1;

-- (not strictly required here, but harmless if you later use the table engine)
SET allow_experimental_materialized_postgresql_table = 1;

CREATE DATABASE IF NOT EXISTS ctv;

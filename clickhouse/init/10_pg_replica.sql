-- Must be in the same session as CREATE DATABASE
SET allow_experimental_database_materialized_postgresql = 1;

-- Create a database that continuously replicates specific PG tables
CREATE DATABASE IF NOT EXISTS pg_ad
ENGINE = MaterializedPostgreSQL('postgres:5432', 'ad_oltp', 'app', 'app')
SETTINGS
    materialized_postgresql_schema = 'public',                 -- one schema
    materialized_postgresql_tables_list = 'advertisers,campaigns,creatives';

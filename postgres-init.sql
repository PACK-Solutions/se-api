-- Create Datadog user with minimal required permissions
CREATE
USER datadog WITH PASSWORD 'datadog';
GRANT pg_monitor TO datadog;
GRANT SELECT ON pg_stat_database TO datadog;

-- Ensure the pg_stat_statements extension is available for query metrics
CREATE
EXTENSION IF NOT EXISTS pg_stat_statements;

-- Create the datadog schema used by helper functions
CREATE SCHEMA IF NOT EXISTS datadog AUTHORIZATION postgres;
REVOKE ALL ON SCHEMA datadog FROM PUBLIC;
GRANT
USAGE
ON
SCHEMA
datadog TO datadog;

-- Helper function used by Datadog DBM to collect EXPLAIN plans
-- SECURITY DEFINER allows the datadog user to execute EXPLAIN safely without broad privileges
CREATE
OR REPLACE FUNCTION datadog.explain_statement(l_query TEXT)
    RETURNS SETOF JSON AS $$
BEGIN
RETURN QUERY EXECUTE FORMAT('EXPLAIN (FORMAT JSON) %s', l_query);
END;
$$
LANGUAGE plpgsql SECURITY DEFINER;

-- Restrict the function search_path to prevent function hijacking
ALTER FUNCTION datadog.explain_statement(TEXT) SET search_path = pg_catalog;

COMMENT
ON SCHEMA datadog IS 'Schema for Datadog helper functions';
COMMENT
ON FUNCTION datadog.explain_statement(TEXT) IS 'Used by Datadog to collect EXPLAIN plans (FORMAT JSON)';

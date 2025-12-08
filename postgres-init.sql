-- Create the personne schema used by the application
CREATE SCHEMA IF NOT EXISTS personne AUTHORIZATION postgres;
REVOKE ALL ON SCHEMA personne FROM PUBLIC;
GRANT
USAGE
ON
SCHEMA
personne TO postgres;

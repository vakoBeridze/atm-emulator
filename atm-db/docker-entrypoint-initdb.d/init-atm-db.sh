#!/bin/bash
set -e

echo "Creating atm-emulator database (POSTGRES_USER=${POSTGRES_USER}, POSTGRES_DB=${POSTGRES_DB}): ..."

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE "atm-emulator";
    CREATE USER "atm" PASSWORD 'thisistoosimple';
    GRANT ALL PRIVILEGES ON DATABASE "atm-emulator" TO "atm";
EOSQL

echo "Creating atm-emulator database: Done."

#!/bin/sh

set -eu

export MYSQL_HOST
export MYSQL_PORT
export MYSQL_USER
export MYSQL_DB_NAME
export SERVER_PORT

echo "DROP DATABASE IF EXISTS $MYSQL_DB_NAME" | mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER"
echo "CREATE DATABASE $MYSQL_DB_NAME" | mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER"
echo 'CREATE TABLE users ( id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY (id) )' | mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" "$MYSQL_DB_NAME"

JAVA_OPTS="-Dhttp.port=$SERVER_PORT"
export JAVA_OPTS
echo "Starting server on :$SERVER_PORT"
exec /opt/docker/bin/aws-xray-example-play-scalikejdbc

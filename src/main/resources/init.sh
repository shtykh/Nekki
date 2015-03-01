#!/bin/sh
psql postgres -c "CREATE USER nekki_user WITH SUPERUSER"
psql postgres -c "CREATE DATABASE nekki OWNER nekki_user"
psql nekki -c "CREATE TABLE IF NOT EXISTS entry  (
	id UUID NOT NULL,
	content VARCHAR(1024) NOT NULL,
	creationDate DATE NOT NULL,
	PRIMARY KEY (id)
)"

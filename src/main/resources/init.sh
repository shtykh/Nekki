#!/bin/sh
psql postgres -c "CREATE USER shtykhnekki_user WITH PASSWORD 'shtykhnekki' SUPERUSER"
psql postgres -c "CREATE DATABASE shtykhnekki OWNER shtykhnekki_user"
psql shtykhnekki -c "CREATE TABLE IF NOT EXISTS entry  (
	id UUID NOT NULL,
	content VARCHAR(1024) NOT NULL,
	creationDate DATE NOT NULL,
	PRIMARY KEY (id)
)"

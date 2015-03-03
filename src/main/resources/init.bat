psql -U postgres -c "CREATE USER shtykhnekki_user WITH PASSWORD 'shtykhnekki' SUPERUSER" postgres
psql -U postgres -c "CREATE DATABASE shtykhnekki OWNER shtykhnekki_user" postgres
psql -U postgres -c "CREATE TABLE IF NOT EXISTS entry (id UUID NOT NULL, content VARCHAR(1024) NOT NULL, creationDate DATE NOT NULL, PRIMARY KEY (id))" shtykhnekki
psql -U postgres -c "ALTER TABLE entry OWNER TO shtykhnekki_user" shtykhnekki

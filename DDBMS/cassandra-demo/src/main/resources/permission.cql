---CREATE USER 'admin' WITH PASSWORD 'admin' SUPERUSER;
---Login with new user
---DROP USER 'cassandra';
---create keyspace mykeyspace;

CREATE USER 'demo' WITH PASSWORD 'demo' NOSUPERUSER;
GRANT ALL PERMISSIONS ON mykeyspace.contact TO demo;
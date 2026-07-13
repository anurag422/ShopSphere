CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO roles(id,name,description) VALUES(gen_random_uuid(),'ROLE_CUSTOMER','Default customer'),(gen_random_uuid(),'ROLE_ADMIN','Administrator'),(gen_random_uuid(),'ROLE_SELLER','Default seller');
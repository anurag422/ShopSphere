CREATE TABLE roles(

   id uuid PRIMARY KEY,
   name VARCHAR(50) NOT NULL UNIQUE,
   description VARCHAR(50)
);

CREATE TABLE user_roles(

     user_id uuid NOT NULL,
     role_id uuid NOT NULL,

     PRIMARY KEY(user_id,role_id),

     CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
     CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles(id)

);
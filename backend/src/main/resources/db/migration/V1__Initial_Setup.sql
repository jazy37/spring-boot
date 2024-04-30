CREATE TABLE customer(
     id BIGSERIAL PRIMARY KEY,
     name TEXT NOT NULL ,
     email TEXT NOT NULL UNIQUE ,
     password TEXT NOT NULL ,
     gender TEXT NOT NULL ,
     age INT NOT NULL
);

CREATE TABLE roles (
       id BIGSERIAL PRIMARY KEY,
       name TEXT NOT NULL
);

CREATE TABLE customer_roles (
        customer_id BIGSERIAL,
        role_id BIGSERIAL,
        FOREIGN KEY (customer_id) REFERENCES customer(id),
        FOREIGN KEY (role_id) REFERENCES roles(id),
        PRIMARY KEY (customer_id, role_id)
);
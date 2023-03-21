CREATE TABLE category (
    id serial,
    name varchar(50) NOT NULL,
    constraint category_pk PRIMARY KEY (id),
    constraint category_name_unique UNIQUE (name)
);

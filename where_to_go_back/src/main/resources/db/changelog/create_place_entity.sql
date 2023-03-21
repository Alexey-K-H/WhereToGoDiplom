CREATE TABLE place (
    id bigserial,
    name varchar(50) NOT NULL,
    description text,
    lat numeric NOT NULL,
    long numeric NOT NULL,
    thumbnail varchar(100),
    uploaded_at date NOT NULL,
    radius int,
    constraint place_pk PRIMARY KEY (id)
);

CREATE TABLE place_category (
    place_id bigint,
    category_id int,
    CONSTRAINT place_category_pk PRIMARY KEY (place_id, category_id),
    CONSTRAINT place_category_fk_category FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT place_category_fk_place FOREIGN KEY (place_id) REFERENCES place(id)
);

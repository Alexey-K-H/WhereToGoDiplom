CREATE TABLE place_pictures (
    id bigserial PRIMARY KEY,
    place_id bigint NOT NULL,
    user_id bigint NOT NULL,
    url varchar(200) NOT NULL,
    CONSTRAINT place_pictures_place_fk FOREIGN KEY (place_id) REFERENCES place(id)
);

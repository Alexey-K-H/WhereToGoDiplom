ALTER TABLE place
    ADD CONSTRAINT place_name_uq UNIQUE (name),
    ADD CONSTRAINT place_desc_uq UNIQUE (description),
    ADD CONSTRAINT place_coords_uq UNIQUE (lat, long);
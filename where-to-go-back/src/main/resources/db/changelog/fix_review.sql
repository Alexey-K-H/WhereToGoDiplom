DROP TABLE review;
CREATE TABLE review(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    place_id BIGINT NOT NULL,
    score INTEGER,
    text TEXT NOT NULL,
    written_at TIMESTAMP NOT NULL,
    CONSTRAINT review_fk_user FOREIGN KEY (user_id)
    REFERENCES users(id),
    CONSTRAINT review_fk_place FOREIGN KEY (place_id)
    REFERENCES place(id)
)

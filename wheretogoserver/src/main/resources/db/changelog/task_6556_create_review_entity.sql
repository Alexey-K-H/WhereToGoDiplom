CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    text TEXT NOT NULL,
    written_at TIMESTAMP NOT NULL,
    CONSTRAINT review_fk FOREIGN KEY (user_id)
    REFERENCES users(id)
)
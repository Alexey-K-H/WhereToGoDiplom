create table users(
    id serial primary key,
    email varchar(50) unique not null,
    username varchar(50) unique not null,
    created_at timestamp not null,
    avatar_link varchar(250),
    avatar_thumbnail_link varchar(250)
)
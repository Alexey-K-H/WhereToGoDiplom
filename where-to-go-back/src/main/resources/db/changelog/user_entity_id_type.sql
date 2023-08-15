alter table review
    drop constraint review_fk;

drop table user_favourites;
drop table user_visited_places;
drop table users;

create table users(
    id bigserial primary key,
    email varchar(50) unique not null,
    username varchar(50) unique not null,
    password varchar(150) not null,
    created_at timestamp not null,
    avatar_link varchar(250),
    avatar_thumbnail_link varchar(250)
);

create table user_visited_places (
    user_id bigint,
    place_id bigint,
    visited_at timestamp,
    constraint user_visited_places_pk primary key (user_id, place_id),
    constraint user_visited_places_fk_user foreign key (user_id) references users(id),
    constraint user_visited_places_fk_place foreign key (place_id) references place(id)
);

create table user_favourites (
    user_id bigint,
    place_id bigint,
    constraint user_favourites_pk primary key (user_id, place_id),
    constraint user_favourites_fk_user foreign key (user_id) references users(id),
    constraint user_favourites_fk_place foreign key (place_id) references place(id)
);

alter table review
    drop column user_id;

alter table review
    add column user_id bigint;

alter table review
    add constraint review_fk foreign key(user_id) references users(id);
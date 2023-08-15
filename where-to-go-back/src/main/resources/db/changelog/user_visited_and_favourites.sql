create table user_visited_places (
    user_id varchar(64),
    place_id bigint,
    visited_at timestamp,
    constraint user_visited_places_pk primary key (user_id, place_id),
    constraint user_visited_places_fk_user foreign key (user_id) references users(id),
    constraint user_visited_places_fk_place foreign key (place_id) references place(id)
);

create table user_favourites (
    user_id varchar(64),
    place_id bigint,
    constraint user_favourites_pk primary key (user_id, place_id),
    constraint user_favourites_fk_user foreign key (user_id) references users(id),
    constraint user_favourites_fk_place foreign key (place_id) references place(id)
);
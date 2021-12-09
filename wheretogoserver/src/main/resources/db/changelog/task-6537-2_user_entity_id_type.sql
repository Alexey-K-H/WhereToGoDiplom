alter table review
drop constraint review_fk;

alter table users
alter column id type varchar(64);

alter table review
alter column user_id type varchar(64);

alter table review
add constraint review_fk foreign key(user_id) references users(id);
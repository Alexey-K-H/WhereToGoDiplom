

create table basedate.categories(
                                    id smallint primary key,
                                    name varchar(50) not null unique
);


create table basedate.places(
                                id integer primary key,
                                name varchar(100) not null,
                                description VARCHAR(500),
                                lat numeric NOT NULL,
                                longg numeric NOT NULL,
                                thumbnail varchar(300) not null,
                                upload_at date not null,
                                average_score real NOT NULL default 0.0,
                                CONSTRAINT place_name_uq UNIQUE (name),
                                CONSTRAINT place_desc_uq UNIQUE (description),
                                CONSTRAINT place_coords_uq UNIQUE (lat, longg)
);

create table basedate.users(
                               id integer primary key,
                               username varchar(80) not null unique ,
                               email varchar(80) not null unique ,
                               password varchar(100) not null ,
                               created_at date not null,
                               avatar_link varchar(200),
                               avatar_thumbnail_link varchar(200)
);


create table basedate.place_pictures(
                                        id integer primary key,
                                        place_id integer not null ,
                                        url varchar(400) not null ,
                                        constraint place_id_fk foreign key (place_id) references places(id)
);


create table basedate.places_categories(
                                           place_id integer not null ,
                                           category_id smallint not null ,
                                           constraint place_category_pk PRIMARY KEY (place_id, category_id),
                                           constraint places_id_fk foreign key (place_id) references places(id),
                                           constraint category_id_fk foreign key (category_id) references categories(id)
);

create table basedate.user_favourites(
                                         id integer primary key,
                                         user_id integer not null ,
                                         place_id integer not null ,
                                         constraint user_fk foreign key (user_id) references users(id),
                                         constraint place_fk foreign key (place_id) references places(id)
);

create table basedate.user_visited_places(
                                             id integer primary key,
                                             user_id integer not null ,
                                             place_id integer not null ,
                                             visited_at date not null,
                                             constraint user_visit_fk foreign key (user_id) references users(id),
                                             constraint place_visit_fk foreign key (place_id) references places(id)
);

create table basedate.place_reviews(
                                       id integer primary key,
                                       place_id integer not null ,
                                       user_id integer not null,
                                       review text not null ,
                                       written_at date,
                                       constraint place_review_fk foreign key (place_id) references places(id),
                                       constraint user_review_fk foreign key (user_id) references users(id)
);




DROP TABLE basedate.places_categories;
DROP TABLE basedate.place_pictures;
DROP TABLE basedate.categories;
DROP TABLE  basedate.user_favourites;
DROP TABLE basedate.place_reviews;
DROP TABLE basedate.user_visited_places;
DROP TABLE basedate.places;
DROP TABLE basedate.users;



INSERT INTO places VALUES (1,'Заброшенный корабль','Более 150-ти барж, катеров, теплоходов и пароходов мертвым грузом лежат на дне и по берегам на огромном участке Обской акватории от Алтая до Салехарда. Вот и это заброшенное судно, отслужившее на воде более полувека, теперь навсегда приковано к берегу', 54.539667, 82.471889,'https://static.tildacdn.com/tild3735-6462-4737-a264-613638373635/___1.png', STR_TO_DATE('08-11-2021','%m-%d-%Y'),0.0);
INSERT INTO places VALUES (2,'Беловский водопад','Беловский водопад расположен в Искитимском районе — недалеко от села Белово. Бурлящий поток воды, срывающийся с пятиметровой высоты, не свойственен для равнинной местности, поэтому после своего загадочного появления водопад быстро превратился в достопримечательность. Беловский водопад окружает живописный березовый лес и просторные поляны. Сказочным видом водопада можно любоваться как летом, наблюдая за стремительным потоком воды, так и зимой, рассматривая причудливые формы застывших волн.', 54.560744, 83.622340,'https://static.tildacdn.com/tild6132-3661-4666-a331-333162643065/64.png', STR_TO_DATE('08-11-2021','%m-%d-%Y'), 0.0);
INSERT INTO places values (3, 'Мира Парк','Необычный парк расположился в 20 км от Новосибирска.«Мира парк» — совершенно новое пространство для нашего города. Он делится на два больших направления: «Парк познания» и «Парк активити». ', 55.1984817, 83.0831566,'https://static.tildacdn.com/tild3636-3836-4431-a234-376666626565/15.png', STR_TO_DATE('08-11-2021','%m-%d-%Y'),0.0 );

INSERT INTO CATEGORIES VALUES (1,'Заброшки');
INSERT INTO BASEDATE.CATEGORIES VALUES (2,'Сооружения');
INSERT INTO BASEDATE.CATEGORIES VALUES (3,'Ночью');
INSERT INTO BASEDATE.CATEGORIES VALUES (4,'Природа');
INSERT INTO BASEDATE.CATEGORIES VALUES (5,'Архитектура');
INSERT INTO BASEDATE.CATEGORIES VALUES (6,'В черте города');
INSERT INTO BASEDATE.CATEGORIES VALUES (7,'В области');



INSERT INTO places_categories VALUES (1,2);
INSERT INTO places_categories VALUES (1,4);
INSERT INTO places_categories VALUES (1,6);

INSERT INTO places_categories VALUES (2,2);
INSERT INTO places_categories VALUES (2,4);
INSERT INTO places_categories VALUES (2,7);

INSERT INTO places_categories VALUES (3,2);
INSERT INTO places_categories VALUES (3,4);
INSERT INTO places_categories VALUES (3,7);
INSERT INTO places_categories VALUES (3,5);



INSERT INTO place_pictures values (1,1,'https://static.tildacdn.com/tild3735-6462-4737-a264-613638373635/___1.png');
INSERT INTO place_pictures values (2,2,'https://static.tildacdn.com/tild6132-3661-4666-a331-333162643065/64.png');
INSERT INTO place_pictures values (3,3,'https://static.tildacdn.com/tild3636-3836-4431-a234-376666626565/15.png');
select * from places;



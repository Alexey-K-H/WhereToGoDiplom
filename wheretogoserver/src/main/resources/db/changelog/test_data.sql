INSERT INTO place (
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (1,'Заброшенный корабль','Более 150-ти барж, катеров, теплоходов и пароходов мертвым грузом лежат на дне и по берегам на огромном участке Обской акватории от Алтая до Салехарда. Вот и это заброшенное судно, отслужившее на воде более полувека, теперь навсегда приковано к берегу', 54.539667, 82.471889,'https://static.tildacdn.com/tild3735-6462-4737-a264-613638373635/___1.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (2,'Беловский водопад','Беловский водопад расположен в Искитимском районе — недалеко от села Белово. Бурлящий поток воды, срывающийся с пятиметровой высоты, не свойственен для равнинной местности.', 54.560744, 83.622340,'https://static.tildacdn.com/tild6132-3661-4666-a331-333162643065/64.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) values (3, 'Мира Парк','Необычный парк расположился в 20 км от Новосибирска.«Мира парк» — совершенно новое пространство для нашего города. Он делится на два больших направления: «Парк познания» и «Парк активити». ', 55.1984817, 83.0831566,'https://static.tildacdn.com/tild3636-3836-4431-a234-376666626565/15.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (4,'Новое яркое пятно в городе','новое колесо обозрения в Новосибирске', 55.00626,  82.93973, 'https://static.tildacdn.com/tild3835-6161-4134-b635-616661363765/104.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (5,'Реконструкция старого города', 'недостроенный исторический комплекс в Бердске', 54.775347, 83.089438, 'https://static.tildacdn.com/tild3564-3537-4533-b732-313634313034/58.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (6,'Феликс Дзержинский','памятник паровозу в Новосибирске',55.02748, 82.90380, 'https://static.tildacdn.com/tild3733-3537-4538-a338-373164333132/53.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (7,'Cтрашно красивое место','Новосибирский крематорий',55.07472, 83.063172, 'https://static.tildacdn.com/tild3262-3264-4764-a133-363061626231/57.png', CURRENT_DATE);

INSERT INTO place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) VALUES (8,'Валадилена','старинное здание в Новосибирске',55.013356, 82.928603,'https://static.tildacdn.com/tild3766-3932-4233-a661-623639366439/23.png', CURRENT_DATE);

insert into place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) values (9,'Тут всегда солнечно','тепличный комплекс в Новосибирске',54.971867, 82.719989,'https://static.tildacdn.com/tild3437-3663-4563-b531-326565336266/72.png', CURRENT_DATE);

insert into place(
    id,
    name,
    description,
    latitude,
    longitude,
    thumbnail,
    uploaded_at
) values (10,'Банкет','брошенная веранда в Новосибирске',55.000683, 83.010108,'https://static.tildacdn.com/tild3339-6334-4138-a232-313530633430/97.png', CURRENT_DATE);

INSERT INTO category VALUES (1,'Заброшки');
INSERT INTO category VALUES (2,'Сооружения');
INSERT INTO category VALUES (3,'Ночью');
INSERT INTO category VALUES (4,'Природа');
INSERT INTO category VALUES (5,'Архитектура');
INSERT INTO category VALUES (6,'В черте города');
INSERT INTO category VALUES (7,'В области');

INSERT INTO place_category VALUES (1,2);
INSERT INTO place_category VALUES (1,4);
INSERT INTO place_category VALUES (1,6);
INSERT INTO place_category VALUES (2,2);
INSERT INTO place_category VALUES (2,4);
INSERT INTO place_category VALUES (2,7);
INSERT INTO place_category VALUES (3,2);
INSERT INTO place_category VALUES (3,4);
INSERT INTO place_category VALUES (3,7);
INSERT INTO place_category VALUES (3,5);
INSERT INTO place_category VALUES (4,2);
INSERT INTO place_category VALUES (4,3);
INSERT INTO place_category VALUES (4,6);
INSERT INTO place_category VALUES (5,2);
INSERT INTO place_category VALUES (5,5);
INSERT INTO place_category VALUES (5,7);
INSERT INTO place_category values (6,2);
INSERT INTO place_category values (6,6);
INSERT INTO place_category values (7,2);
INSERT INTO place_category values (7,6);
INSERT INTO place_category values (8,2);
INSERT INTO place_category values (8,6);
INSERT INTO place_category values (9,2);
INSERT INTO place_category values (9,7);
INSERT INTO place_category values (10,2);
INSERT INTO place_category values (10,6);
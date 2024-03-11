INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        52,
        'Единственный дебаркадер в Новосибирске',
        'Заброшенный речной порт в НСО',
        54.3578611,
        81.9713611,
        'https://static.tildacdn.com/tild6235-3437-4331-b039-663235653733/18.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (52, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (52, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (52, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (52, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 52;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        53,
        'Железный берег',
        'Сооружения, укрепляющие пляж в Бердске',
        54.7723889,
        83.1458333,
        'https://static.tildacdn.com/tild3162-3631-4264-b561-393962633536/21.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (53, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (53, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (53, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 53;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        54,
        'Заброшенные корабли',
        'Стоянка старых барж в Новосибирске',
        55.0275278,
        82.8651667,
        'https://static.tildacdn.com/tild3665-3664-4162-b965-323233346235/1.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (54, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (54, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (54, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 54;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        55,
        'Заброшенные машины и старая техника',
        'Брошенные автомобили в Новосибирске',
        54.8548889,
        83.1218611,
        'https://static.tildacdn.com/tild6361-6265-4638-a562-336261346262/___1.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (55, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (55, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (55, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 55;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        56,
        'Заброшенный детский лагерь',
        'Старый лагерь в Новосибирске',
        54.7853889,
        83.1101667,
        'https://static.tildacdn.com/tild6433-3563-4535-b465-623039656264/146.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (56, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (56, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (56, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 56;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        57,
        'Заброшенный речной порт',
        'Брошенные речные краны в НСО',
        55.5244612,
        83.4876519,
        'https://static.tildacdn.com/tild3039-3139-4263-a565-326235623266/photo.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (57, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (57, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (57, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 57;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        58,
        'Забытая железная дорога',
        'Старая железная дорога в Новосибирске',
        55.0969167,
        82.9564722,
        'https://static.tildacdn.com/tild3863-6462-4165-b337-626438343535/132.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (58, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (58, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 58;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        59,
        'Забытые кабинеты',
        'Брошенный кабинет в Новосибирске',
        55.0381944,
        82.92375,
        'https://static.tildacdn.com/tild6234-3865-4336-b738-353331653134/92.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (59, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (59, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 59;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        60,
        'Завершение пляжного сезона',
        'Пляж, лучший в Бердске',
        54.7690278,
        83.0159444,
        'https://static.tildacdn.com/tild6535-6437-4630-a662-623663343433/80.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (60, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (60, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 60;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        61,
        'Закатище',
        'Сооружения шлюза в Новосибирске',
        54.8341944,
        83.0435278,
        'https://static.tildacdn.com/tild6266-3763-4837-a265-663338326132/195.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (61, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (61, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 61;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        62,
        'Заложники бетонных плит',
        'Бетонные коробки в Новосибирске',
        55.0169444,
        83.0069722,
        '',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (62, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (62, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 62;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        63,
        'Заправка из прошлого',
        'Заброшенная АЗС в Новосибирске',
        54.9440278,
        83.2363889,
        'https://static.tildacdn.com/tild3936-3636-4139-b466-313337663836/75.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (63, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (63, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 63;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        64,
        'Запрещённое море',
        'Сооружения шлюза в Новосибирске',
        54.8359722,
        83.0465278,
        'https://static.tildacdn.com/tild6664-3761-4433-a666-656164613933/79.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (64, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (64, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (64, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 64;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        65,
        'Засекреченная сказка',
        'Строительные леса и водопад в Новосибирске',
        54.8461944,
        82.9974444,
        'https://static.tildacdn.com/tild6361-6135-4966-b064-353339393839/83.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (65, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (65, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (65, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 65;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        66,
        'Затерянный в пространстве',
        'Геометрическое сооружение в Новосибирске',
        55.0000833,
        83.0024167,
        'https://static.tildacdn.com/tild3937-6165-4964-a461-646563653936/96.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (66, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (66, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 66;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        67,
        'Затишье перед бурей',
        'Рукотворный пирс в Бердске',
        54.7182294,
        83.024564,
        'https://static.tildacdn.com/tild6262-3034-4465-a133-386137623466/___1.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (67, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (67, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (67, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 67;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        68,
        'Затхлость',
        'Заброшенный санаторий в Бердске',
        54.7897684,
        83.0980327,
        'https://static.tildacdn.com/tild3961-3964-4239-b630-326161303462/166.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (68, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (68, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 68;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        69,
        'Здесь будет город-сад!',
        'Брошенный дом в Новосибирске',
        54.965694,
        82.9056454,
        'https://static.tildacdn.com/tild3533-3038-4633-b036-326362313631/99.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (69, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (69, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 69;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        70,
        'Золотистое поле',
        'Пшеничные поля в Новосибирске',
        54.9249722,
        83.1865,
        'https://static.tildacdn.com/tild3439-3365-4232-b734-613536633432/7.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (70, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (70, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 70;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        71,
        'Игры в ночных огнях',
        'Шлюзовой маяк в Новосибирске',
        55.00223,
        82.9106913,
        'https://static.tildacdn.com/tild6539-6237-4566-a261-393832613164/111.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (71, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (71, 3);
INSERT INTO T_PLACE_CATEGORY
VALUES (71, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 71;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        72,
        'Игры со светом',
        'Коридоры стадиона в Новосибирске',
        55.0381944,
        82.92375,
        'https://static.tildacdn.com/tild3865-6631-4966-b334-323661306430/91.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (72, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (72, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 72;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        73,
        'Изучаем фактурные стены',
        'Фактурная стена в Новосибирске',
        55.0720833,
        82.94225,
        'https://static.tildacdn.com/tild3864-6237-4763-b235-333931343864/127.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (73, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (73, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 73;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        74,
        'Изучение местности',
        'Красивое место в Караканском бору',
        54.2572778,
        81.7353611,
        'https://static.tildacdn.com/tild3062-3564-4437-b539-656533316264/100.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (74, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (74, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 74;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        75,
        'Индастриал',
        'Брошенный кирпичный завод в Бердске',
        54.7138889,
        83.0986111,
        'https://static.tildacdn.com/tild3636-3062-4266-a539-663834373139/148.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (75, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (75, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 75;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        76,
        'Индустриальная красота',
        'Большие краны в Новосибирске',
        55.0155833,
        82.9145833,
        'https://static.tildacdn.com/tild3961-3739-4264-a439-626330383038/115.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (76, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (76, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 76;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        77,
        'Индустриальные задворки',
        'Двор-колодец в Новосибирске',
        55.0295556,
        82.9186667,
        'https://static.tildacdn.com/tild3831-6435-4135-b062-366638316233/78.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (77, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 77;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        78,
        'Искусственный водопад',
        'Действующий водопад в НСО',
        55.0528889,
        83.7307222,
        'https://static.tildacdn.com/tild6132-3661-4666-a331-333162643065/64.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (78, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (78, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (78, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 78;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        79,
        'Искусство прошлого',
        'Мозаичная стена в Новосибирске',
        54.9720833,
        82.8971111,
        'https://static.tildacdn.com/tild3731-3331-4034-a162-353065333138/95.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (79, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (79, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 79;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        80,
        'Калифорния',
        'Железная лестница в Новосибирске',
        55.0133131,
        82.9264313,
        'https://static.tildacdn.com/tild3066-6536-4162-b030-363539326133/30.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (80, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (80, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (80, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 80;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        81,
        'Кладбище старых барж',
        'Пляж и заброшенные баржи',
        54.3584167,
        81.9281389,
        'https://static.tildacdn.com/tild3636-3136-4832-b136-363137373339/11.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (81, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (81, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (81, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 81;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        82,
        'Колоннада',
        'Небольшой шлюз в Новосибирске',
        54.9183333,
        82.8126111,
        'https://static.tildacdn.com/tild3064-6264-4166-b561-333135373930/48.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (82, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (82, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 82;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        83,
        'Красота в простоте',
        'Пляж на обском водохранилище',
        54.8337222,
        83.0506667,
        'https://static.tildacdn.com/tild3261-3866-4637-a139-646133383562/134.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (83, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (83, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 83;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        84,
        'Красота во тьме',
        'Сгоревший зал кинотеатра в Бердске',
        54.7886944,
        83.096,
        'https://static.tildacdn.com/tild3663-3035-4661-b261-343036616338/171.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (84, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (84, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 84;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        85,
        'Красота вокруг старейших санаториев',
        'Понтонный мост и старый пирс в Бердске',
        54.7220556,
        83.0300833,
        'https://static.tildacdn.com/tild6539-6462-4466-b133-623639316433/145.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (85, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (85, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (85, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 85;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        86,
        'Красота забытого',
        'Заброшенный санаторий в Бердске',
        54.7906389,
        83.0955,
        'https://static.tildacdn.com/tild3738-6538-4966-a332-633931313865/154.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (86, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (86, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 86;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        87,
        'Крылатые качели',
        'Громадные качели на пляже в Новосибирске',
        54.82625,
        83.0823333,
        'https://static.tildacdn.com/tild6263-3435-4132-b236-656336633265/27.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (87, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (87, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (87, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 87;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        88,
        'Кто мы?',
        'Трибуны в Бердске',
        54.7919444,
        83.03875,
        'https://static.tildacdn.com/tild6366-6635-4031-b737-396665343265/___1.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (88, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (88, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (88, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 88;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        89,
        'Лестница с вьюном',
        'Лестница с вьюном в Новосибирске',
        54.8428042,
        83.1236253,
        'https://static.tildacdn.com/tild3362-3033-4131-a565-326364323231/___1.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (89, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (89, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 89;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        90,
        'Летнее наслаждение',
        'Маяк на пляже в Новосибирске',
        54.84143,
        82.9489313,
        'https://static.tildacdn.com/tild6332-3961-4164-b236-373161396131/186.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (90, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (90, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (90, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 90;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        91,
        'Летние горнолыжники',
        'Горнолыжный комплекс в Новосибирске',
        54.9837778,
        83.0457778,
        'https://static.tildacdn.com/tild6265-6461-4139-b539-396331313263/124.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (91, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (91, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 91;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        92,
        'Лучик света в тёмном царстве',
        'Недостроенное здание в Новосибирске',
        55.00075,
        83.0035556,
        'https://static.tildacdn.com/tild3866-6531-4466-a432-376264663166/47.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (92, 1);
INSERT INTO T_PLACE_CATEGORY
VALUES (92, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (92, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 92;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        93,
        'Лучшее место для встречи заката',
        'Самые красивые закаты на этом пляже в Бердске',
        54.7991944,
        83.0532778,
        'https://static.tildacdn.com/tild6661-3334-4564-b530-356332323431/130.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (93, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (93, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 93;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        94,
        'Лучший вид на город!',
        'Смотровая площадка с видом на правый берег города Новосибирск',
        54.97275,
        82.9555278,
        'https://static.tildacdn.com/tild6533-3832-4333-b763-396566386532/135.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (94, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (94, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 94;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        95,
        'Лучший пляж',
        'Пляж в Бердске',
        54.8000833,
        83.0552222,
        'https://static.tildacdn.com/tild3632-3065-4230-a337-616436346336/141.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (95, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (95, 7);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 95;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        96,
        'Любите открывать новое',
        'Горы коряг в Новосибирске',
        54.8436944,
        82.9622778,
        'https://static.tildacdn.com/tild3534-3637-4962-b935-343061666131/183.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (96, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (96, 4);
INSERT INTO T_PLACE_CATEGORY
VALUES (96, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 96;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        97,
        'Масштабы',
        'Горы угля в Новосибирске',
        55.0177778,
        82.9091111,
        'https://static.tildacdn.com/tild6166-6462-4430-a130-363639316539/163.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (97, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (97, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 97;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        98,
        'Местные Мальдивы',
        'Золоотвал в Новосибирске',
        55.0083889,
        83.10275,
        'https://static.tildacdn.com/tild6536-3837-4036-b964-303038336363/137.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (98, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (98, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 98;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        99,
        'Место прибывания коряг',
        'Завод, в которой скапливаются старые деревья в Новосибирске<br />',
        54.8415833,
        83.0329444,
        'https://static.tildacdn.com/tild3134-6534-4530-b337-323132303265/122.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (99, 2);
INSERT INTO T_PLACE_CATEGORY
VALUES (99, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 99;

INSERT INTO T_PLACE (
                    ID,
                    NAME,
                    DESCRIPTION,
                    LATITUDE,
                    LONGITUDE,
                    THUMBNAIL,
                    UPLOADED_AT)
VALUES (
        100,
        'Металлик',
        'Металлическая стена в Новосибирске',
        55.0391944,
        82.9061667,
        'https://static.tildacdn.com/tild6238-6662-4865-b365-343364643966/180.png',
        CURRENT_DATE);

INSERT INTO T_PLACE_CATEGORY
VALUES (100, 5);
INSERT INTO T_PLACE_CATEGORY
VALUES (100, 6);

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE ID = 100;
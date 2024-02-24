CREATE OR REPLACE FUNCTION RANDOM_BETWEEN(LOW INT, HIGH INT)
    RETURNS INT AS
$$
BEGIN
    RETURN FLOOR(RANDOM() * (HIGH - LOW + 1) + LOW);
END;
$$ LANGUAGE PLPGSQL STRICT;

CREATE OR REPLACE FUNCTION CREATE_RANDOM_DURATION()
    RETURNS INT AS
$$
BEGIN
    RETURN RANDOM_BETWEEN(15, 40);
END;
$$ LANGUAGE PLPGSQL STRICT;

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Заброшенный корабль';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Беловский водопад';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Мира парк';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Новое яркое пятно в городе';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Реконструкция старого города';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Феликс дзержинский';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Страшно красивое место';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Валадилена';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Тут всегда солнечно';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Банкет';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Архитектурное наслаждение';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Безграничность';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Безлюдный обрыв';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Белая соль';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Белена';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Брошенный остров';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Вечное зарево';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'В мире литературы';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'В окружении красок';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Геометрия';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Весенний контраст';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Второе самое красивое место';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Действующая психиатрическая больница';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Громадный шар посреди поля';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Единение с природой';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Восстановление';

UPDATE T_PLACE
SET DURATION = CREATE_RANDOM_DURATION()
WHERE NAME LIKE 'Масштабная забытость';
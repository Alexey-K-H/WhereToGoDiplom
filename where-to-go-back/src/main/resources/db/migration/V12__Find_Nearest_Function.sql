--Упрощенные функции без индексов
DROP FUNCTION IF EXISTS FILTER_CANDIDATES;
CREATE OR REPLACE FUNCTION FILTER_CANDIDATES(
    MY_LAT DOUBLE PRECISION,
    MY_LON DOUBLE PRECISION,
    DLAT DOUBLE PRECISION,
    DLON DOUBLE PRECISION,
    _LIMIT INT,
    _MAX_DIST DOUBLE PRECISION,
    DEG2DIST DOUBLE PRECISION)
    RETURNS TABLE
            (
                ID          BIGINT,
                LATITUDE    NUMERIC,
                LONGITUDE   NUMERIC,
                DESCRIPTION VARCHAR(255),
                NAME        VARCHAR(255),
                THUMBNAIL   VARCHAR(255),
                UPLOADED_AT TIMESTAMP,
                DIST        DOUBLE PRECISION
            )
    LANGUAGE PLPGSQL
AS
$$
BEGIN
    RETURN QUERY (WITH CTE1
                           AS (SELECT V_PLACES_INFO.ID,
                                      V_PLACES_INFO.LATITUDE,
                                      V_PLACES_INFO.LONGITUDE,
                                      V_PLACES_INFO.DESCRIPTION,
                                      V_PLACES_INFO.NAME,
                                      V_PLACES_INFO.THUMBNAIL,
                                      V_PLACES_INFO.UPLOADED_AT,
                                      V_PLACES_INFO.CATEGORY_ID,
                                      @DEG2DIST * GCDIST(@MY_LAT, @MY_LON, V_PLACES_INFO.LATITUDE,
                                                         V_PLACES_INFO.LONGITUDE) AS DIST
                               FROM V_PLACES_INFO
                               WHERE V_PLACES_INFO.LATITUDE BETWEEN @MY_LAT - @DLAT
                                   AND @MY_LAT + @DLAT
                                 AND V_PLACES_INFO.LONGITUDE BETWEEN @MY_LON - @DLON
                                   AND @MY_LON + @DLON
                                 AND @DEG2DIST * GCDIST(@MY_LAT, @MY_LON, V_PLACES_INFO.LATITUDE,
                                                        V_PLACES_INFO.LONGITUDE) <= _MAX_DIST
                               ORDER BY DIST, V_PLACES_INFO.ID)
                  SELECT DISTINCT CTE1.ID,
                                  CTE1.LATITUDE,
                                  CTE1.LONGITUDE,
                                  CTE1.DESCRIPTION,
                                  CTE1.NAME,
                                  CTE1.THUMBNAIL,
                                  CTE1.UPLOADED_AT,
                                  CTE1.DIST
                  FROM CTE1
                  ORDER BY CTE1.DIST
                  LIMIT _LIMIT);
END;
$$;

COMMENT ON FUNCTION FILTER_CANDIDATES(
    MY_LAT DOUBLE PRECISION,
    MY_LON DOUBLE PRECISION,
    DLAT DOUBLE PRECISION,
    DLON DOUBLE PRECISION,
    _LIMIT INT,
    _MAX_DIST DOUBLE PRECISION,
    DEG2DIST DOUBLE PRECISION)
    IS 'Функция фильтрации множества найденных кандидатов';

DROP FUNCTION IF EXISTS FIND_NEAREST;
CREATE FUNCTION FIND_NEAREST(
    _MY_LAT DOUBLE PRECISION, /*Широта в пределах [-90...90]*/
    _MY_LON DOUBLE PRECISION, /*Долгота в пределах [-180...180]*/
    _START_DIST DOUBLE PRECISION, /*Начальная оценка расстояния поиска*/
    _MAX_DIST DOUBLE PRECISION, /*Максимальная дистанция где искать*/
    _LIMIT INT /*Сколько найденных кандидатов нужно (делать сильно большим не стоит, чтобы не нагружать карту)*/
)
    RETURNS TABLE
            (
                ID          BIGINT,
                LATITUDE    NUMERIC,
                LONGITUDE   NUMERIC,
                DESCRIPTION VARCHAR(255),
                NAME        VARCHAR(255),
                THUMBNAIL   VARCHAR(255),
                UPLOADED_AT TIMESTAMP,
                DIST        DOUBLE PRECISION
            )
AS
$$
DECLARE
    _DEG2RAD   DOUBLE PRECISION DEFAULT PI() / 180;
    MY_LAT     DOUBLE PRECISION := _MY_LAT;
    MY_LON     DOUBLE PRECISION := _MY_LON;
    DEG2DIST   DOUBLE PRECISION := 69.172; /*69.172 for miles; 111.325 for km  *** (mi vs km)*/
    START_DEG  DOUBLE PRECISION := _START_DIST / @DEG2DIST; /*начальный радиус поиска*/
    MAX_DEG    DOUBLE PRECISION := _MAX_DIST / @DEG2DIST; /*максимальный радиус поиска*/
    CUTOFF     DOUBLE PRECISION := @MAX_DEG / SQRT(2);
    DLAT       DOUBLE PRECISION := @START_DEG;
    DLON       DOUBLE PRECISION;
    LON2LAT    DOUBLE PRECISION := COS(_DEG2RAD * @MY_LAT);
    ITERATIONS INT              := 0;
    NEAR_CT    INT;
    WEST_LON   DOUBLE PRECISION;
    EAST_LON   DOUBLE PRECISION;

BEGIN

    <<MAINLOOP>>
    LOOP
        ITERATIONS := @ITERATIONS + 1;
        DLON := ABS(@DLAT / @LON2LAT);

        IF ABS(@MY_LAT) + @DLAT >= 90
        THEN
            DLON := 361;
        ELSE
            --nothing
        END IF;

        NEAR_CT = FIND_NEAR_CT(MY_LAT := MY_LAT, MY_LON := MY_LON, DLAT := DLAT, DLON := DLON);

        IF (@NEAR_CT >= _LIMIT OR
            @DLAT >= @CUTOFF) THEN
            EXIT MAINLOOP;/*Выход из цикла*/
        END IF;
        /*Расширяем квадрат поиска*/
        DLAT := LEAST(2 * @DLAT, @CUTOFF);/*Удваиваем радиус поиска*/
    END LOOP
        MAINLOOP;


    /*Мы вышли из цикла, если нашли достаточно мест, либо вышли за пределы граничных условий и сдались*/
    /*Теперь нужно получить информацию о найденных местах, если они есть*/
    /*Причем отобрать нужное количество, ибо мы могли найти больше*/

    IF (@DLAT >= @MAX_DEG OR @DLON >= 180)
    THEN
        DLAT := @MAX_DEG;
    ELSE
        DLAT := GCDIST(ABS(@MY_LAT), @MY_LON,
                       ABS(@MY_LAT) - @DLAT, @MY_LON - @DLON);
    END IF;

    DLON := COALESCE(ASIN(SIN(_DEG2RAD * @DLAT) / COS(_DEG2RAD * @MY_LAT)) / _DEG2RAD, 361);

    /*Последний запрос, который берет информацию об отобранных кандидатах*/
    /*Если мы не вышли в отрицательные координаты*/
    IF (ABS(@MY_LON) + @DLON < 180 OR
        ABS(@MY_LAT) + @DLAT < 90) THEN
        RETURN QUERY (SELECT *
                      FROM FILTER_CANDIDATES(
                              MY_LAT := MY_LAT,
                              MY_LON := MY_LON,
                              DLAT := DLAT,
                              DLON := DLON,
                              _LIMIT := _LIMIT,
                              _MAX_DIST := _MAX_DIST,
                              DEG2DIST := DEG2DIST));
    ELSE
        /*Если вышли в отрицательные координаты, то пересчитываем*/
        CASE
            WHEN @MY_LON < 0
                THEN WEST_LON := @MY_LON;
            ELSE WEST_LON := @MY_LON - 360;
            END CASE;

        EAST_LON := @WEST_LON + 360;

        RETURN QUERY (SELECT *
                      FROM FILTER_CANDIDATES(
                              MY_LAT := MY_LAT,
                              MY_LON := WEST_LON,
                              DLAT := DLAT,
                              DLON := DLON,
                              _LIMIT := _LIMIT,
                              _MAX_DIST := _MAX_DIST,
                              DEG2DIST := DEG2DIST));
    END IF;
END;
$$ LANGUAGE PLPGSQL;

COMMENT ON FUNCTION FIND_NEAREST(
    _MY_LAT DOUBLE PRECISION,
    _MY_LON DOUBLE PRECISION,
    _START_DIST DOUBLE PRECISION,
    _MAX_DIST DOUBLE PRECISION,
    _LIMIT INT
    )
    IS 'Основная функция поиска ближайших мест к геоточке'
DROP FUNCTION IF EXISTS FIND_NEAREST_WITH_VISITED_IDS;
CREATE FUNCTION FIND_NEAREST_WITH_VISITED_IDS(
    _MY_LAT DOUBLE PRECISION,
    _MY_LON DOUBLE PRECISION,
    _START_DIST DOUBLE PRECISION,
    _MAX_DIST DOUBLE PRECISION,
    _LIMIT INTEGER,
    _IDS TEXT
)
    RETURNS TABLE
            (
                ID          BIGINT,
                LATITUDE    NUMERIC,
                LONGITUDE   NUMERIC,
                DESCRIPTION CHARACTER VARYING,
                NAME        CHARACTER VARYING,
                THUMBNAIL   CHARACTER VARYING,
                UPLOADED_AT TIMESTAMP WITHOUT TIME ZONE,
                DIST        DOUBLE PRECISION
            )
    LANGUAGE PLPGSQL
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
    IDS        INT ARRAY;
BEGIN
    IDS = STRING_TO_ARRAY(_IDS, ',');

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
    /*Причем отобрать нужное количетсво, ибо мы могли найти больше*/

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
                      FROM FILTER_CANDIDATES_BY_VISITED_IDS(
                              MY_LAT := MY_LAT,
                              MY_LON := MY_LON,
                              DLAT := DLAT,
                              DLON := DLON,
                              _IDS := IDS,
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
                      FROM FILTER_CANDIDATES_BY_VISITED_IDS(
                              MY_LAT := MY_LAT,
                              MY_LON := WEST_LON,
                              DLAT := DLAT,
                              DLON := DLON,
                              _IDS := IDS,
                              _LIMIT := _LIMIT,
                              _MAX_DIST := _MAX_DIST,
                              DEG2DIST := DEG2DIST));
    END IF;
END;
$$;

COMMENT ON FUNCTION FIND_NEAREST_WITH_VISITED_IDS(
    _MY_LAT DOUBLE PRECISION,
    _MY_LON DOUBLE PRECISION,
    _START_DIST DOUBLE PRECISION,
    _MAX_DIST DOUBLE PRECISION,
    _LIMIT INTEGER,
    _IDS TEXT)
    IS 'Основная функция поиска набора мест вблизи заданной геоточки с учетом посещенных мест';
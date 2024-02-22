DROP FUNCTION IF EXISTS GET_PLACES_BY_COORDS;
CREATE FUNCTION GET_PLACES_BY_COORDS(_LATS TEXT, _LONS TEXT)
    RETURNS TABLE
                (
                    ID          BIGINT,
                    LATITUDE    NUMERIC,
                    LONGITUDE   NUMERIC,
                    DESCRIPTION CHARACTER VARYING,
                    NAME        CHARACTER VARYING,
                    THUMBNAIL   CHARACTER VARYING,
                    UPLOADED_AT TIMESTAMP WITHOUT TIME ZONE
                )
    LANGUAGE PLPGSQL
AS
$$
BEGIN
    RETURN QUERY (SELECT V_PLACES_INFO.ID,
                         V_PLACES_INFO.LATITUDE,
                         V_PLACES_INFO.LONGITUDE,
                         V_PLACES_INFO.DESCRIPTION,
                         V_PLACES_INFO.NAME,
                         V_PLACES_INFO.THUMBNAIL,
                         V_PLACES_INFO.UPLOADED_AT
                         FROM V_PLACES_INFO
                         WHERE V_PLACES_INFO.LATITUDE IN (_LATS) AND V_PLACES_INFO.LONGITUDE IN (_LONS)
                         ORDER BY V_PLACES_INFO.NAME);
END
$$;

COMMENT ON FUNCTION GET_PLACES_BY_COORDS(_LATS TEXT, _LONS TEXT) IS 'Функция получения всех мест из БД в соответствии с координатами';
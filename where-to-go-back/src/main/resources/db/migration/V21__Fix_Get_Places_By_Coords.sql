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
DECLARE
    LATS NUMERIC ARRAY;
	LONS NUMERIC ARRAY;
BEGIN
	LATS = STRING_TO_ARRAY(_LATS, ',');
	LONS = STRING_TO_ARRAY(_LONS, ',');

    RETURN QUERY (WITH CTE1 AS (SELECT V_PLACES_INFO.ID,
                         V_PLACES_INFO.LATITUDE,
                         V_PLACES_INFO.LONGITUDE,
                         V_PLACES_INFO.DESCRIPTION,
                         V_PLACES_INFO.NAME,
                         V_PLACES_INFO.THUMBNAIL,
                         V_PLACES_INFO.UPLOADED_AT
                         FROM V_PLACES_INFO
                         WHERE V_PLACES_INFO.LATITUDE = ANY(LATS) AND V_PLACES_INFO.LONGITUDE = ANY(LONS)
                         ORDER BY V_PLACES_INFO.NAME)
				  SELECT DISTINCT CTE1.ID,
                                  CTE1.LATITUDE,
                                  CTE1.LONGITUDE,
                                  CTE1.DESCRIPTION,
                                  CTE1.NAME,
                                  CTE1.THUMBNAIL,
                                  CTE1.UPLOADED_AT
                  FROM CTE1);
END
$$;

COMMENT ON FUNCTION GET_PLACES_BY_COORDS(_LATS TEXT, _LONS TEXT) IS 'Функция получения всех мест из БД в соответствии с координатами';
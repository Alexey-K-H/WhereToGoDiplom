--Функция фильтрации множества найденных кандидатов с учетом посещенных мест
DROP FUNCTION IF EXISTS FILTER_CANDIDATES_BY_VISITED_IDS;
CREATE OR REPLACE FUNCTION FILTER_CANDIDATES_BY_VISITED_IDS(
    MY_LAT DOUBLE PRECISION,
    MY_LON DOUBLE PRECISION,
    DLAT DOUBLE PRECISION,
    DLON DOUBLE PRECISION,
    _IDS INT[],
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
                                 AND V_PLACES_INFO.ID != ANY (_IDS)
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

COMMENT ON FUNCTION FILTER_CANDIDATES_BY_VISITED_IDS(
    MY_LAT DOUBLE PRECISION,
    MY_LON DOUBLE PRECISION,
    DLAT DOUBLE PRECISION,
    DLON DOUBLE PRECISION,
    _IDS INT[],
    _LIMIT INT,
    _MAX_DIST DOUBLE PRECISION,
    DEG2DIST DOUBLE PRECISION)
    IS 'Функция фильтрации множества найденных кандидатов с учетом посещенных мест';
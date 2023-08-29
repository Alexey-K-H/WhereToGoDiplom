DROP FUNCTION IF EXISTS GCDIST;
CREATE FUNCTION GCDIST(
    _LAT1 DOUBLE PRECISION, /*Масштабирование в градусах на север для одной точки*/
    _LON1 DOUBLE PRECISION, /*Масштабирование в градусах на запад для одной точки*/
    _LAT2 DOUBLE PRECISION,
    _LON2 DOUBLE PRECISION
) RETURNS DOUBLE PRECISION
    IMMUTABLE
AS
$$
    -- Константы
DECLARE
    _DEG2RAD       DOUBLE PRECISION DEFAULT PI() / 180;
    DECLARE _RLAT1 DOUBLE PRECISION DEFAULT _DEG2RAD * _LAT1;
    DECLARE _RLAT2 DOUBLE PRECISION DEFAULT _DEG2RAD * _LAT2;
    DECLARE _RLOND DOUBLE PRECISION DEFAULT _DEG2RAD * (_LON1 - _LON2);
    DECLARE _M     DOUBLE PRECISION DEFAULT COS(_RLAT2);
    DECLARE _X     DOUBLE PRECISION DEFAULT COS(_RLAT1) - _M * COS(_RLOND);
    DECLARE _Y     DOUBLE PRECISION DEFAULT _M * SIN(_RLOND);
    DECLARE _Z     DOUBLE PRECISION DEFAULT SIN(_RLAT1) - SIN(_RLAT2);
    DECLARE _N     DOUBLE PRECISION DEFAULT SQRT(
                _X * _X +
                _Y * _Y +
                _Z * _Z
    );
BEGIN
    RETURN 2 * ASIN(_N / 2) / _DEG2RAD;
END
$$ LANGUAGE PLPGSQL;

COMMENT ON FUNCTION GCDIST(
    _LAT1 DOUBLE PRECISION, /*Масштабирование в градусах на север для одной точки*/
    _LON1 DOUBLE PRECISION, /*Масштабирование в градусах на запад для одной точки*/
    _LAT2 DOUBLE PRECISION,
    _LON2 DOUBLE PRECISION
    )
    IS 'GCDist - это вспомогательная ФУНКЦИЯ, которая правильно вычисляет расстояние между двумя точками на земном шаре';
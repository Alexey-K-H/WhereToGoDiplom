# GCDist - это вспомогательная ФУНКЦИЯ, которая правильно вычисляет расстояние между двумя точками на земном шаре.

drop function if exists GCDist;
create function GCDist(
    _lat1 double, /*Масштабирование в градусах на север для одной точки*/
    _lon1 double, /*Масштабирование в градусах на запад для одной точки*/
    _lat2 double,
    _lon2 double
)returns double
    deterministic
    contains sql
    sql security invoker
begin
    #     Константы
    declare _deg2rad double default pi()/180;
    declare _rlat1 double default _deg2rad * _lat1;
    declare _rlat2 double default _deg2rad * _lat2;

    declare _rlond double default _deg2rad * (_lon1 - _lon2);
    declare _m double default cos(_rlat2);
    declare _x double default cos(_rlat1) - _m * cos(_rlond);
    declare _y double default _m * sin(_rlond);
    declare _z double default sin(_rlat1) - sin(_rlat2);
    declare _n double default sqrt(
                    _x * _x +
                    _y * _y +
                    _z * _z
        );
    return 2 * ASIN(_n/2) / _deg2rad;
end;

# Основная функция поиска набора мест вблизи заданной геоточки.
# Используется как для поиска ближайших мест от пользователя, так и для поиска мест,
# расопложенных недлаеко от мест, посещенных пользователем в процессе использования приложения.
drop procedure if exists FindNearestByVisited;
create procedure FindNearestByVisited(
    in _my_lat double, /*Широта в пределах [-90...90]*/
    in _my_lon double, /*Долгота в пределах [-180...180]*/
    in _START_dist double, /*Начальня оценка расстояния поиска*/
    in _max_dist double, /*Максимальная дистанция где искать*/
    in _limit int, /*Сколько найденных кандидатов нужно (делать сильно большим не стоит, чтобы не нагружать карту)*/
    in _ids varchar(255) /*Индексы параметров для отбора мест, которых нет в списке посещенных*/
)
    deterministic
begin
    /*Константы*/
    declare _deg2rad double default PI()/180;

    /*Нельзя использовать параметры, поэтому используем переменные*/
    set
        @my_lat := _my_lat,
        @my_lon := _my_lon,
        @deg2dist := 69.172, /*69.172 for miles; 111.325 for km  *** (mi vs km)*/
        @start_deg := _START_dist / @deg2dist, /*начальный радиус поиска*/
        @max_deg := _max_dist / @deg2dist, /*максимальный радиус поиска*/
        @cutoff := @max_deg / sqrt(2),
        @dlat := @start_deg,
        @lon2lat := COS(_deg2rad * @my_lat),
        @iterations := 0;

    /*Начинаем цикл поиска, каждый раз увличивая квадрат карты для поиска.
    Повторяем до тех пор пока не найдем нужное количество точек или пока не достигнем предела.*/

    /*Запрос для разбиения данных по широте и долготе (из таблицы с метсами)*/
    set @sql =
            'select count(*)
                into @near_ct
                from place
                where latitude between @my_lat - @dlat
                    and @my_lat + @dlat
                  and longitude between @my_lon - @dlon
                    and @my_lon + @dlon';

    prepare _sql from @sql;
    MainLoop: LOOP
        set @iterations := @iterations + 1;
        set @dlon := ABS(@dlat / @lon2lat);
        set @dlon := if(ABS(@my_lat) + @dlat >= 90, 361, @dlon);
        execute _sql;
        if(@near_ct >= _limit or
           @dlat >= @cutoff) then
            leave MainLoop;/*Выход из цикла*/
        end if;
        /*Расширяем квадрат поиска*/
        set @dlat := LEAST(2 * @dlat, @cutoff);/*Удваиваем радиус поиска*/
    end loop MainLoop;
    deallocate prepare _sql;

    /*Мы вышли из цикла, если нашли достаточно мест, либо вышли за пределы граничных условий и сдались*/
    /*Теперь нужно получить информацию о найденных местах, если они есть*/
    /*Причем отобрать нужное количетсво, ибо мы могли найти больше*/
    set @dlat := if(@dlat >= @max_deg or @dlon >= 180,
                    @max_deg,
                    GCDist(ABS(@my_lat), @my_lon,
                           ABS(@my_lat) - @dlat, @my_lon - @dlon));

    set @dlon := ifnull(asin(sin(_deg2rad * @dlat) /
                             cos(_deg2rad * @my_lat))
                            / _deg2rad, 361);

    /*Последний запрос, который берет информауию об отобранных кандидатах*/
    /*Если мы не вышли в отрициатльные координаты*/
    if(abs(@my_lon) + @dlon < 180 or
       abs(@my_lat) + @dlat < 90) then
        set @sql  = concat(
                'with cte1
                as(
                select places_info.id, latitude, longitude, description, name, thumbnail, uploaded_at, category_id,
                @deg2dist * GCDist(@my_lat, @my_lon, latitude, longitude) as dist
                from places_info
                    where latitude between @my_lat - @dlat
                        and @my_lat + @dlat
                    and longitude between @my_lon - @dlon
                        and @my_lon + @dlon
                    and places_info.id not in (', _ids, ')
            having dist <= ', _max_dist, '
            order by dist, id
        )
        select distinct cte1.id, latitude, longitude, description, name, thumbnail, uploaded_at, dist
        from cte1 limit ', _limit);
    else
        /*Если вышли в отрицательные координаты, то пересчитываем*/
        set @west_lon := if(@my_lon < 0, @my_lon, @my_lon - 360);
        set @east_lon := @west_lon + 360;

        set @sql  = concat(
                'with cte1
                as(
                select places_info.id, latitude, longitude, description, name, thumbnail, uploaded_at, category_id,
                @deg2dist * GCDist(@my_lat, @west_lon, latitude, longitude) as dist
                from places_info
                    where latitude between @my_lat - @dlat
                        and @my_lat + @dlat
                    and longitude between @west_lon - @dlon
                        and @west_lon + @dlon
                    and places_info.id not in (', _ids, ')
            having dist <= ', -_max_dist, '
            order by dist, id
            )
            select distinct cte1.id, latitude, longitude, description, name, thumbnail, uploaded_at, dist
            from cte1 limit ', _limit);
    end if;

    prepare _sql from @sql;
    execute _sql;
    deallocate prepare _sql;
end;
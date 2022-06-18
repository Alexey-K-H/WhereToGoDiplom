-- GCDist - это вспомогательная ФУНКЦИЯ, которая правильно вычисляет расстояние между двумя точками на земном шаре.

drop function if exists GCDist;
create function GCDist(
    _lat1 double precision, /*Масштабирование в градусах на север для одной точки*/
    _lon1 double precision, /*Масштабирование в градусах на запад для одной точки*/
    _lat2 double precision,
    _lon2 double precision
) returns double precision
    immutable
as
$$
    -- Константы
declare
    _deg2rad       double precision default pi() / 180;
    declare _rlat1 double precision default _deg2rad * _lat1;
    declare _rlat2 double precision default _deg2rad * _lat2;
    declare _rlond double precision default _deg2rad * (_lon1 - _lon2);
    declare _m     double precision default cos(_rlat2);
    declare _x     double precision default cos(_rlat1) - _m * cos(_rlond);
    declare _y     double precision default _m * sin(_rlond);
    declare _z     double precision default sin(_rlat1) - sin(_rlat2);
    declare _n     double precision default sqrt(
                _x * _x +
                _y * _y +
                _z * _z
    );
begin
    return 2 * ASIN(_n / 2) / _deg2rad;
end
$$ LANGUAGE plpgsql;


--Функция поиска мест кандидатов
create or replace function where_to_go.find_near_ct(my_lat double precision, my_lon double precision, dlat double precision, dlon double precision)
    returns integer
    language plpgsql as
$$BEGIN
    return (select count(*) as x
            from where_to_go.place
            where latitude between (@my_lat - @dlat)
                and (@my_lat + @dlat)
              and longitude between (@my_lon - @dlon)
                and (@my_lon + @dlon));
end;
$$;


--Функция фильрации множества нацденных кандидатов
drop function if exists where_to_go.filter_candidates_with_ids;
create or replace function where_to_go.filter_candidates_with_ids(
    my_lat double precision,
    my_lon double precision,
    dlat double precision,
    dlon double precision,
    _ids int[],
    _limit int,
    _max_dist double precision,
    deg2dist double precision,
    is_in_indexes boolean)
    returns table(
                     id bigint,
                     latitude numeric,
                     longitude numeric,
                     description varchar(255),
                     name varchar(255),
                     thumbnail varchar(255),
                     uploaded_at timestamp,
                     dist double precision
                 )
    language plpgsql as
$$
BEGIN
    if is_in_indexes = true then
        return query (with cte1
                               as (select places_info.id,
                                          places_info.latitude,
                                          places_info.longitude,
                                          places_info.description,
                                          places_info.name,
                                          places_info.thumbnail,
                                          places_info.uploaded_at,
                                          places_info.category_id,
                                          @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) as dist
                                   from where_to_go.places_info
                                   where places_info.latitude between @my_lat - @dlat
                                       and @my_lat + @dlat
                                     and places_info.longitude between @my_lon - @dlon
                                       and @my_lon + @dlon
                                     and where_to_go.places_info.category_id = any (_ids)
                                     and @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) <= _max_dist
                                   order by dist, places_info.id)
                      select distinct cte1.id,
                                      cte1.latitude,
                                      cte1.longitude,
                                      cte1.description,
                                      cte1.name,
                                      cte1.thumbnail,
                                      cte1.uploaded_at,
                                      cte1.dist
                      from cte1
                      order by cte1.dist
                      limit _limit);
    else
        return query (with cte1
                               as (select places_info.id,
                                          places_info.latitude,
                                          places_info.longitude,
                                          places_info.description,
                                          places_info.name,
                                          places_info.thumbnail,
                                          places_info.uploaded_at,
                                          places_info.category_id,
                                          @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) as dist
                                   from where_to_go.places_info
                                   where places_info.latitude between @my_lat - @dlat
                                       and @my_lat + @dlat
                                     and places_info.longitude between @my_lon - @dlon
                                       and @my_lon + @dlon
                                     and where_to_go.places_info.category_id != any (_ids)
                                     and @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) <= _max_dist
                                   order by dist, places_info.id)
                      select distinct cte1.id,
                                      cte1.latitude,
                                      cte1.longitude,
                                      cte1.description,
                                      cte1.name,
                                      cte1.thumbnail,
                                      cte1.uploaded_at,
                                      cte1.dist
                      from cte1
                      order by cte1.dist
                      limit _limit);
    end if;
end;
$$;



-- Основная функция поиска набора мест вблизи заданной геоточки.
-- Используется как для поиска ближайших мест от пользователя, так и для поиска мест,
-- расопложенных недлаеко от мест, посещенных пользователем в процессе использования приложения.
drop function if exists where_to_go.findnearestWithIds;
create function where_to_go.FindNearestByCategory(
    _my_lat double precision, /*Широта в пределах [-90...90]*/
    _my_lon double precision, /*Долгота в пределах [-180...180]*/
    _START_dist double precision, /*Начальня оценка расстояния поиска*/
    _max_dist double precision, /*Максимальная дистанция где искать*/
    _limit int, /*Сколько найденных кандидатов нужно (делать сильно большим не стоит, чтобы не нагружать карту)*/
    _ids int[], /*Индексы параметров для отбора мест по категориям*/
    is_in_indexes boolean
)returns table(
                  id bigint,
                  latitude numeric,
                  longitude numeric,
                  description varchar(255),
                  name varchar(255),
                  thumbnail varchar(255),
                  uploaded_at timestamp,
                  dist double precision
              )
as
$$
declare _deg2rad double precision default PI()/180;
        my_lat double precision := _my_lat;
        my_lon double precision := _my_lon;
        deg2dist double precision := 69.172; /*69.172 for miles; 111.325 for km  *** (mi vs km)*/
        start_deg double precision := _START_dist / @deg2dist; /*начальный радиус поиска*/
        max_deg double precision := _max_dist / @deg2dist; /*максимальный радиус поиска*/
        cutoff double precision := @max_deg / sqrt(2);
        dlat double precision := @start_deg;
        dlon double precision;
        lon2lat double precision := COS(_deg2rad * @my_lat);
        iterations int := 0;

        near_ct int;

        west_lon double precision;
        east_lon double precision;

begin

    <<MainLoop>>
    LOOP
        iterations := @iterations + 1;
        dlon := ABS(@dlat / @lon2lat);

        if ABS(@my_lat) + @dlat >= 90
        then
            dlon := 361;
        else
            --nothing
        end if;

        near_ct = where_to_go.find_near_ct(my_lat := my_lat, my_lon := my_lon, dlat := dlat, dlon := dlon);

        if(@near_ct >= _limit or
           @dlat >= @cutoff) then
            exit MainLoop;/*Выход из цикла*/
        end if;
        /*Расширяем квадрат поиска*/
        dlat := LEAST(2 * @dlat, @cutoff);/*Удваиваем радиус поиска*/
    end loop
        MainLoop;


    /*Мы вышли из цикла, если нашли достаточно мест, либо вышли за пределы граничных условий и сдались*/
    /*Теперь нужно получить информацию о найденных местах, если они есть*/
    /*Причем отобрать нужное количетсво, ибо мы могли найти больше*/

    if (@dlat >= @max_deg or @dlon >= 180)
    then
        dlat := @max_deg;
    else
        dlat := where_to_go.GCDist(ABS(@my_lat), @my_lon,
                                   ABS(@my_lat) - @dlat, @my_lon - @dlon);
    end if;

    dlon := COALESCE(asin(sin(_deg2rad * @dlat) / cos(_deg2rad * @my_lat)) / _deg2rad, 361);

    /*Последний запрос, который берет информауию об отобранных кандидатах*/
    /*Если мы не вышли в отрициатльные координаты*/
    if(abs(@my_lon) + @dlon < 180 or
       abs(@my_lat) + @dlat < 90) then
        return query (select * from where_to_go.filter_candidates_with_ids(
                my_lat := my_lat,
                my_lon := my_lon,
                dlat := dlat,
                dlon := dlon,
                _ids := _ids,
                _limit := _limit,
                _max_dist := _max_dist,
                deg2dist := deg2dist,
                is_in_indexes := is_in_indexes));
    else
        /*Если вышли в отрицательные координаты, то пересчитываем*/
        CASE WHEN @my_lon < 0
                 THEN
                     west_lon := @my_lon;
             ELSE
                 west_lon := @my_lon - 360;
            END CASE;

        east_lon := @west_lon + 360;

        return query (select * from where_to_go.filter_candidates_with_ids(
                my_lat := my_lat,
                my_lon := west_lon,
                dlat := dlat,
                dlon := dlon,
                _ids := _ids,
                _limit := _limit,
                _max_dist := _max_dist,
                deg2dist := deg2dist,
                is_in_indexes := is_in_indexes));
    end if;
end;
$$LANGUAGE plpgsql;

--Пример вызова финальной функции
select * from where_to_go.findnearestWithIds(
        _my_lat := 55.00867, _my_lon := 82.93807, _start_dist := 1, _max_dist := 2, _limit := 2, _ids := '{1,2,3,4,5,6,7}', is_in_indexes :=  true
    );

--Функция получения всех мест из БД в соответсвтии с категориями
drop function if exists where_to_go.getPlaces;
create or replace function where_to_go.getPlaces(
    _ids int[]
)
    returns table
            (
                id          bigint,
                latitude    numeric,
                longitude   numeric,
                description varchar(255),
                name        varchar(255),
                thumbnail   varchar(255),
                uploaded_at timestamp
            )
    language plpgsql
as
$$
BEGIN
    return query (with cte1 as (select places_info.id,
                                       places_info.latitude,
                                       places_info.longitude,
                                       places_info.description,
                                       places_info.name,
                                       places_info.thumbnail,
                                       places_info.uploaded_at,
                                       places_info.category_id
                                from places_info
                                where places_info.category_id = any (_ids)
                                order by places_info.name)
                  select distinct cte1.id, cte1.latitude, cte1.longitude, cte1.description, cte1.name, cte1.thumbnail, cte1.uploaded_at
                  from cte1);
end
$$;

--Пример выхова функции получения мест
select * from where_to_go.getplaces('{1,2,3,4,5,6,7}');


--Упрощенные функции без индексов

--Функция фильрации множества нацденных кандидатов
drop function if exists where_to_go.filter_candidates;
create or replace function where_to_go.filter_candidates(
    my_lat double precision,
    my_lon double precision,
    dlat double precision,
    dlon double precision,
    _limit int,
    _max_dist double precision,
    deg2dist double precision)
    returns table(
                     id bigint,
                     latitude numeric,
                     longitude numeric,
                     description varchar(255),
                     name varchar(255),
                     thumbnail varchar(255),
                     uploaded_at timestamp,
                     dist double precision
                 )
    language plpgsql as
$$
BEGIN
    return query (with cte1
                           as (select places_info.id,
                                      places_info.latitude,
                                      places_info.longitude,
                                      places_info.description,
                                      places_info.name,
                                      places_info.thumbnail,
                                      places_info.uploaded_at,
                                      places_info.category_id,
                                      @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) as dist
                               from where_to_go.places_info
                               where places_info.latitude between @my_lat - @dlat
                                   and @my_lat + @dlat
                                 and places_info.longitude between @my_lon - @dlon
                                   and @my_lon + @dlon
                                 and @deg2dist * where_to_go.GCDist(@my_lat, @my_lon, places_info.latitude, places_info.longitude) <= _max_dist
                               order by dist, places_info.id)
                  select distinct cte1.id,
                                  cte1.latitude,
                                  cte1.longitude,
                                  cte1.description,
                                  cte1.name,
                                  cte1.thumbnail,
                                  cte1.uploaded_at,
                                  cte1.dist
                  from cte1
                  order by cte1.dist
                  limit _limit);
end;
$$;

drop function if exists where_to_go.findnearest;
create function where_to_go.FindNearest(
    _my_lat double precision, /*Широта в пределах [-90...90]*/
    _my_lon double precision, /*Долгота в пределах [-180...180]*/
    _START_dist double precision, /*Начальня оценка расстояния поиска*/
    _max_dist double precision, /*Максимальная дистанция где искать*/
    _limit int /*Сколько найденных кандидатов нужно (делать сильно большим не стоит, чтобы не нагружать карту)*/
)returns table(
                  id bigint,
                  latitude numeric,
                  longitude numeric,
                  description varchar(255),
                  name varchar(255),
                  thumbnail varchar(255),
                  uploaded_at timestamp,
                  dist double precision
              )
as
$$
declare _deg2rad double precision default PI()/180;
        my_lat double precision := _my_lat;
        my_lon double precision := _my_lon;
        deg2dist double precision := 69.172; /*69.172 for miles; 111.325 for km  *** (mi vs km)*/
        start_deg double precision := _START_dist / @deg2dist; /*начальный радиус поиска*/
        max_deg double precision := _max_dist / @deg2dist; /*максимальный радиус поиска*/
        cutoff double precision := @max_deg / sqrt(2);
        dlat double precision := @start_deg;
        dlon double precision;
        lon2lat double precision := COS(_deg2rad * @my_lat);
        iterations int := 0;

        near_ct int;

        west_lon double precision;
        east_lon double precision;

begin

    <<MainLoop>>
    LOOP
        iterations := @iterations + 1;
        dlon := ABS(@dlat / @lon2lat);

        if ABS(@my_lat) + @dlat >= 90
        then
            dlon := 361;
        else
            --nothing
        end if;

        near_ct = where_to_go.find_near_ct(my_lat := my_lat, my_lon := my_lon, dlat := dlat, dlon := dlon);

        if(@near_ct >= _limit or
           @dlat >= @cutoff) then
            exit MainLoop;/*Выход из цикла*/
        end if;
        /*Расширяем квадрат поиска*/
        dlat := LEAST(2 * @dlat, @cutoff);/*Удваиваем радиус поиска*/
    end loop
        MainLoop;


    /*Мы вышли из цикла, если нашли достаточно мест, либо вышли за пределы граничных условий и сдались*/
    /*Теперь нужно получить информацию о найденных местах, если они есть*/
    /*Причем отобрать нужное количетсво, ибо мы могли найти больше*/

    if (@dlat >= @max_deg or @dlon >= 180)
    then
        dlat := @max_deg;
    else
        dlat := where_to_go.GCDist(ABS(@my_lat), @my_lon,
                                   ABS(@my_lat) - @dlat, @my_lon - @dlon);
    end if;

    dlon := COALESCE(asin(sin(_deg2rad * @dlat) / cos(_deg2rad * @my_lat)) / _deg2rad, 361);

    /*Последний запрос, который берет информауию об отобранных кандидатах*/
    /*Если мы не вышли в отрициатльные координаты*/
    if(abs(@my_lon) + @dlon < 180 or
       abs(@my_lat) + @dlat < 90) then
        return query (select * from where_to_go.filter_candidates(
                my_lat := my_lat,
                my_lon := my_lon,
                dlat := dlat,
                dlon := dlon,
                _limit := _limit,
                _max_dist := _max_dist,
                deg2dist := deg2dist));
    else
        /*Если вышли в отрицательные координаты, то пересчитываем*/
        CASE WHEN @my_lon < 0
                 THEN
                     west_lon := @my_lon;
             ELSE
                 west_lon := @my_lon - 360;
            END CASE;

        east_lon := @west_lon + 360;

        return query (select * from where_to_go.filter_candidates(
                my_lat := my_lat,
                my_lon := west_lon,
                dlat := dlat,
                dlon := dlon,
                _limit := _limit,
                _max_dist := _max_dist,
                deg2dist := deg2dist));
    end if;
end;
$$LANGUAGE plpgsql;

--Пример вызова функции
select * from where_to_go.findnearest(
        _my_lat := 55.00867, _my_lon := 82.93807, _start_dist := 1, _max_dist := 2, _limit := 2);
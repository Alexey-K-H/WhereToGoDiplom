drop procedure if exists GetPlaces;
create procedure GetPlaces(
    in _ids varchar(255) /*Индексы параметров для отбора мест по категориям*/
)
    deterministic
begin
    set @sql  = concat(
            'with cte1 as(
            select places_info.id, latitude, longitude, description, name, thumbnail, uploaded_at, category_id
            from places_info
                    where places_info.category_id in (', _ids, ')
        order by places_info.name)
        select distinct cte1.id, latitude, longitude, description, name, thumbnail, uploaded_at
        from cte1'
        );

    prepare _sql from @sql;
    execute _sql;
    deallocate prepare _sql;
end;
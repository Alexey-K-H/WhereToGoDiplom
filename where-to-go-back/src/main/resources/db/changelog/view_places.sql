#Содержит полную информацию о местах, в том числе их категории
drop view if exists places_info;
create view places_info
as
select place.*, category_id from place
                                     inner join place_category pc on place.id = pc.place_id
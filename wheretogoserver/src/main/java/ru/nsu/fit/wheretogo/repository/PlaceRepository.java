package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.entity.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByNameOrDescriptionOrCoords(String name, String description, Coords coords);

//    Процедура поиска ближайших мест к конкретной точке на карте
//    Используется как для поиска ближайших мест от пользователя, так и для поиска мест,
//    расопложенных недлаеко от мест, посещенных пользователем в процессе использования приложения
    @Query(value = "call FindNearest(:_my_lat, :_my_lon, :_START_dist, :_max_dist, :_limit); -- #pageable", nativeQuery = true)
    public Page<Place> findNearestPlaces(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            Pageable pageable
    );

    //TODO:добавить две функции для поиска ближайших по ктаегориям, а также по поесещнным

}

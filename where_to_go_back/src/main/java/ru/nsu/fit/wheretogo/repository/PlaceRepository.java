package ru.nsu.fit.wheretogo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.common.Coords;
import ru.nsu.fit.wheretogo.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByNameOrDescriptionOrCoords(String name, String description, Coords coords);

    @Query(value = "select * from where_to_go.getplaces(:_ids) -- #pageable", nativeQuery = true)
    public Page<Place> getPlaces(
            @Param("_ids") String categoryIds,
            Pageable pageable
    );

    //    Процедура поиска ближайших мест к конкретной точке на карте
    @Query(value = "select * from where_to_go.findnearest(:_my_lat, :_my_lon, :_START_dist, :_max_dist, :_limit) -- #pageable",
            nativeQuery = true)
    public Page<Place> findNearestPlaces(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            Pageable pageable
    );

    //Процедура поиска ближайших мест по конкретной точке с учетом категорий
    @Query(value = "select * from where_to_go.findnearestwithids(:_my_lat, :_my_lon, :_START_dist, :_max_dist, :_limit, :_ids, true) -- #pageable",
            nativeQuery = true)
    public Page<Place> findNearestByCategory(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            @Param("_ids") String categoryIds,
            Pageable pageable
    );

    //Процедура поиска блмажйших мест по конкретной точке с учетом посещений пользователя
    @Query(value = "select * from where_to_go.findnearestwithids(:_my_lat, :_my_lon, :_START_dist, :_max_dist, :_limit, :_ids, false) -- #pageable",
            nativeQuery = true)
    public Page<Place> findNearestByVisited(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            @Param("_ids") String visitedIds,
            Pageable pageable
    );

    //Поиск мест, не посещенных пользователем
    //Необходимо для составления рекомендаций на основе контента
    @Query(value = "select * from where_to_go.place where id not in (select place_id from where_to_go.user_visited_places where user_id = :_user_id) -- #pageable",
            nativeQuery = true)
    public Page<Place> findNotVisitedByUser(
            @Param("_user_id") Long userId,
            Pageable pageable
    );
}

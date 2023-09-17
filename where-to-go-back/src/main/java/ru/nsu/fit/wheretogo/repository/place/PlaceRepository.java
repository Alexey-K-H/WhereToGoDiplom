package ru.nsu.fit.wheretogo.repository.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.fit.wheretogo.entity.place.Coordinates;
import ru.nsu.fit.wheretogo.entity.place.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByNameOrDescriptionOrCoordinates(String name, String description, Coordinates coordinates);

    /**
     * Получение списка мест, в соответствии со списком категорий
     */
    @Query(value = "SELECT * FROM GET_PLACES(:_ids)", nativeQuery = true)
    List<Place> getPlaces(
            @Param("_ids") String categoryIds
    );

    /**
     * Процедура поиска ближайших мест к конкретной точке на карте.
     */
    @Query(value = "SELECT * FROM FIND_NEAREST(" +
            ":_my_lat," +
            " :_my_lon," +
            " :_START_dist," +
            " :_max_dist," +
            " :_limit)",
            nativeQuery = true)
    List<Place> findNearestPlaces(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit
    );

    /**
     * Процедура поиска ближайших мест по конкретной точке с учетом категорий.
     */
    @Query(value = "SELECT * FROM FIND_NEAREST_WITH_CATEGORY_IDS(" +
            ":_my_lat," +
            " :_my_lon," +
            " :_START_dist," +
            " :_max_dist," +
            " :_limit," +
            " :_ids)",
            nativeQuery = true)
    List<Place> findNearestByCategory(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            @Param("_ids") String categoryIds
    );

    /**
     * Процедура поиска ближайших мест по конкретной точке с учетом посещений пользователя.
     */
    @Query(value = "SELECT * FROM FIND_NEAREST_WITH_VISITED_IDS(" +
            ":_my_lat," +
            " :_my_lon," +
            " :_START_dist," +
            " :_max_dist," +
            " :_limit," +
            " :_ids)",
            nativeQuery = true)
    List<Place> findNearestByVisited(
            @Param("_my_lat") Double lat,
            @Param("_my_lon") Double lon,
            @Param("_START_dist") Double startDist,
            @Param("_max_dist") Double maxDist,
            @Param("_limit") Integer limit,
            @Param("_ids") String visitedIds
    );

    /**
     * Поиск мест, не посещенных пользователем.
     * Необходимо для составления рекомендаций на основе контента.
     */
    @Query(value = "SELECT * FROM T_PLACE WHERE ID NOT IN (" +
            "SELECT PLACE_ID FROM T_USER_VISITED_PLACES WHERE USER_ID = :_user_id)",
            nativeQuery = true)
    List<Place> findNotVisitedByUser(
            @Param("_user_id") Long userId
    );
}

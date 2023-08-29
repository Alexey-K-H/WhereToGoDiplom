package ru.nsu.fit.wheretogo.service.recommender;

import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

/**
 * Сервис по работе с рекомендациями
 */
public interface RecommenderService {

    /**
     * Получение списка ближайших мест к определенной точке на карте с учетом выбранных категорий
     *
     * @param myLat
     * @param myLon
     * @param startDist
     * @param maxDist
     * @param limit
     * @param categoryIds
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getNearestPlacesByCategory(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            String categoryIds,
            int page,
            int size
    );

    /**
     * Получение списка мест, расположенных близко к точкам остановок пользователя
     *
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getNearestPlacesByStayPoints(
            int page,
            int size
    );

    /**
     * Получение списка ближайших мест к тем, которые пользователь посетил
     *
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getNearestPlacesByVisited(
            int page,
            int size
    );

    /**
     * Получение списка рекомендаций на основе контента
     *
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getContentBasedRecommendations(
            int page,
            int size
    );

    /**
     * Получение списка рекомендаций на основе коллаборативной фильтрации по оценкам
     *
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByScores(
            int page,
            int size
    );

    /**
     * Получение списка рекомендаций на основе коллаборативной фильтрации по избранным
     *
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getCollaborativeRecommendationsByFavourites(
            int page,
            int size
    );
}

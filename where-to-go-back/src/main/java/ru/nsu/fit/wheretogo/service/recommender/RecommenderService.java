package ru.nsu.fit.wheretogo.service.recommender;

import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.route.RouteRecommendResponse;

import java.util.List;

/**
 * Сервис по работе с рекомендациями
 */
public interface RecommenderService {

    /**
     * Получение списка ближайших мест к определенной точке на карте с учетом выбранных категорий
     *
     * @param myLat       широта точки
     * @param myLon       долгота точки
     * @param startDist   начальный радиус поиска
     * @param maxDist     максимальный радиус поиска
     * @param limit       максимальное число мест
     * @param categoryIds идентификаторы выбранных категорий
     * @return список мест
     */
    List<PlaceBriefDTO> getNearestPlacesByCategory(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            String categoryIds
    );

    /**
     * Получение списка мест, расположенных близко к точкам остановок пользователя
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getNearestPlacesByStayPoints();

    /**
     * Получение списка ближайших мест к тем, которые пользователь посетил
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getNearestPlacesByVisited();

    /**
     * Получение списка рекомендаций на основе контента
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getContentBasedRecommendations();

    /**
     * Получение списка рекомендаций на основе коллаборативной фильтрации по оценкам
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getCollaborativeRecommendationsByScores();

    /**
     * Получение списка рекомендаций на основе коллаборативной фильтрации по избранным
     *
     * @return список мест
     */
    List<PlaceBriefDTO> getCollaborativeRecommendationsByFavourites();

    /**
     * Получение маршрута-рекомендации (машина)
     *
     * @return маршрут
     */
    RouteRecommendResponse getRouteRecommendationDriving();

    /**
     * Получение маршрута-рекомендации (пешком)
     *
     * @return маршрут
     */
    RouteRecommendResponse getRouteRecommendationWalking();
}

package ru.nsu.fit.wheretogo.service.place;

import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;

import java.util.List;

/**
 * Сервис для работы с местами
 */
public interface PlaceService {

    /**
     * Добавление места
     *
     * @param place данные запроса
     */
    void addPlace(PlaceDescriptionDTO place);

    /**
     * Удаление места
     *
     * @param place данные запроса
     */
    void deletePlace(PlaceDescriptionDTO place);

    /**
     * Получение места по идентификатору
     *
     * @param id идентификатор места
     * @return данные места
     */
    PlaceDescriptionDTO getPlaceById(Long id);

    /**
     * Получение списка ближайших мест к определенной точке на карте,
     * заданной координатами (myLat, myLon)
     *
     * @param myLat     широта
     * @param myLon     долгота
     * @param startDist начальный радиус поиска
     * @param maxDist   конечный радиус поиска
     * @param limit     максимальное количество мест
     * @return список мест
     */
    List<PlaceBriefDTO> getNearestPlacesToPoint(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit
    );

    /**
     * Получение списка мест, относящихся к выбранным категориям
     *
     * @param categoryIds список идентификаторов категорий
     * @return список мест
     */
    List<PlaceBriefDTO> getPlaces(
            String categoryIds
    );

    /**
     * Добавление категории конкретному месту
     *
     * @param placeId    идентификатор места
     * @param categoryId идентификатор категории
     */
    void addPlaceCategory(Long placeId, Long categoryId);


}

package ru.nsu.fit.wheretogo.service.place;

import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;

/**
 * Сервис для работы с местами
 */
public interface PlaceService {

    /**
     * @param place
     */
    void addPlace(PlaceDescriptionDTO place);

    /**
     * @param place
     */
    void deletePlace(PlaceDescriptionDTO place);

    /**
     * @param id
     * @return
     */
    PlaceDescriptionDTO getPlaceById(Long id);

    /**
     * Получение списка ближайших мест к определенной точке на карте,
     * заданной координатами (myLat, myLon)
     *
     * @param myLat
     * @param myLon
     * @param startDist
     * @param maxDist
     * @param limit
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getNearestPlacesToPoint(
            double myLat,
            double myLon,
            double startDist,
            double maxDist,
            int limit,
            int page,
            int size
    );

    /**
     * @param categoryIds
     * @param page
     * @param size
     * @return
     */
    PagedListDTO<PlaceBriefDTO> getPlaces(
            String categoryIds,
            int page,
            int size
    );

    /**
     * @param placeId
     * @param categoryId
     */
    void addPlaceCategory(Long placeId, Long categoryId);


}

package ru.nsu.fit.wheretogo.utils.helpers.nearest.search;

import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;

import java.util.List;

/**
 * Сервис по поиску ближайших мест к координатам
 */
public interface NearestSearchHelper {

    /**
     * Поиск ближайших мест к точке
     *
     * @param point точка, заданная своими координатами
     * @param limit максимальное число мест, которое необходимо
     * @return список мест
     */
    List<LatLong> findNearestPlaces2Point(LatLong point, Integer limit, double startRange, double maxRange);
}

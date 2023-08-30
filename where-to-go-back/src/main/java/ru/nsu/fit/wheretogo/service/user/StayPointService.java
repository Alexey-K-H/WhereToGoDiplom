package ru.nsu.fit.wheretogo.service.user;

import ru.nsu.fit.wheretogo.dto.user.StayPointDTO;

import java.util.List;

/**
 * Сервис для работы с геоточками пользователя
 */
public interface StayPointService {

    /**
     * Добавить точку остановки пользователя
     *
     * @param lat широта
     * @param lon долгота
     */
    void addStoryPoint(double lat, double lon);

    /**
     * Удалить неактуальную точку остановки пользователя
     */
    void deleteOldStoryPoint();

    /**
     * Получить точки остановок пользователя
     *
     * @return список геоточек
     */
    List<StayPointDTO> getByUser();

    /**
     * Проверка, что пользователь имеет точки остановок
     *
     * @return true или false
     */
    boolean ifUserHasStayPoints();

}

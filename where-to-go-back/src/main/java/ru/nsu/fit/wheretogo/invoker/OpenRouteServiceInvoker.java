package ru.nsu.fit.wheretogo.invoker;

import ru.nsu.fit.wheretogo.dto.route.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.dto.route.model.LatLong;

import java.util.List;

/**
 * Инвокер для Open Route Service
 */
public interface OpenRouteServiceInvoker {

    /**
     * Проверка работоспособности ORS
     *
     * @return статус
     */
    String health();

    /**
     * Получить путь, используя способ передвижения "машина"
     *
     * @param keyPlacesCoordinates ключевые точки маршрута
     * @return путь
     */
    ORSDirectionResponse getDirectionDriving(List<LatLong> keyPlacesCoordinates);

    /**
     * Получить путь, используя способ передвижения "пешком"
     *
     * @param keyPlacesCoordinates ключевые точки маршрута
     * @return путь
     */
    ORSDirectionResponse getDirectionWalking(List<LatLong> keyPlacesCoordinates);
}

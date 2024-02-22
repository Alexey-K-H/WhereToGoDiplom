package ru.nsu.fit.wheretogo.service.openroute;

import ru.nsu.fit.wheretogo.dto.route.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.dto.route.model.LatLong;

import java.util.List;

/**
 * Сервис по работе с OpenRoute сервером
 */
public interface OpenRouteService {

    /**
     * Проверка работоспособности
     *
     * @return статус
     */
    String health();

    /**
     * Получить маршрут для типа передвижения "машина"
     *
     * @param keyPoints список ключевых мест (их координаты)
     * @return маршрут
     */
    ORSDirectionResponse getDirectionDriving(List<LatLong> keyPoints);

    /**
     * Получить маршрут для типа передвижения "пешком"
     *
     * @param keyPoints список ключевых мест (их координаты)
     * @return маршрут
     */
    ORSDirectionResponse getDirectionWalking(List<LatLong> keyPoints);
}

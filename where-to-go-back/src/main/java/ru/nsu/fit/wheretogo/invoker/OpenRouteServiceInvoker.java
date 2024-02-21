package ru.nsu.fit.wheretogo.invoker;

import ru.nsu.fit.wheretogo.dto.route.ORSDirectionsDto;

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
     * @return путь
     */
    ORSDirectionsDto getDirectionDriving();

    /**
     * Получить путь, используя способ передвижения "машина"
     *
     * @return путь
     */
    ORSDirectionsDto getDirectionWalking();
}

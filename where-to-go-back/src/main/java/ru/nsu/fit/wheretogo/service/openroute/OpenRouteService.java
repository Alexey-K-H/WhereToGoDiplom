package ru.nsu.fit.wheretogo.service.openroute;

import ru.nsu.fit.wheretogo.dto.route.ORSDirectionResponse;

/**
 * Сервис по работе с OpenRoute сервером
 */
public interface OpenRouteService {

    String health();

    ORSDirectionResponse getDirectionDriving();

    ORSDirectionResponse getDirectionWalking();
}

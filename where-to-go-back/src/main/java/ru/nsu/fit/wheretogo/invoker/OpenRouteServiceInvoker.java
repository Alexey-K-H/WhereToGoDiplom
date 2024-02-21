package ru.nsu.fit.wheretogo.invoker;

import ru.nsu.fit.wheretogo.dto.route.ORSDirectionsDto;

public interface OpenRouteServiceInvoker {
    String health();
    ORSDirectionsDto getDirectionDriving();
    ORSDirectionsDto getDirectionWalking();
}

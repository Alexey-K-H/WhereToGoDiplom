package ru.nsu.fit.wheretogo.service.openroute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.route.ORSDirectionsDto;
import ru.nsu.fit.wheretogo.invoker.OpenRouteServiceInvoker;

@RequiredArgsConstructor
@Service
public class OpenRouteServiceImpl implements OpenRouteService {

    private final OpenRouteServiceInvoker openRouteServiceInvoker;

    @Override
    public String health() {
        return openRouteServiceInvoker.health();
    }

    @Override
    public ORSDirectionsDto getDirectionDriving() {
        return openRouteServiceInvoker.getDirectionDriving();
    }

    @Override
    public ORSDirectionsDto getDirectionWalking() {
        return null;
    }
}

package ru.nsu.fit.wheretogo.service.openroute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.dto.route.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.dto.route.model.LatLong;
import ru.nsu.fit.wheretogo.invoker.OpenRouteServiceInvoker;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenRouteServiceImpl implements OpenRouteService {

    private final OpenRouteServiceInvoker openRouteServiceInvoker;

    @Override
    public String health() {
        return openRouteServiceInvoker.health();
    }

    @Override
    public ORSDirectionResponse getDirectionDriving(List<LatLong> keyPoints) {
        return openRouteServiceInvoker.getDirectionDriving(keyPoints);
    }

    @Override
    public ORSDirectionResponse getDirectionWalking(List<LatLong> keyPoints) {
        return openRouteServiceInvoker.getDirectionWalking(keyPoints);
    }
}

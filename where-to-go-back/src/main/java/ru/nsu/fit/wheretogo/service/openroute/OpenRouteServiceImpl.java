package ru.nsu.fit.wheretogo.service.openroute;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.wheretogo.invoker.OpenRouteServiceInvoker;
import ru.nsu.fit.wheretogo.model.ors.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.model.ors.ORSMatrixRequest;
import ru.nsu.fit.wheretogo.model.ors.ORSMatrixResponse;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;

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

    @Override
    public ORSMatrixResponse getPlacesDurationMatrix(ORSMatrixRequest request) {
        return openRouteServiceInvoker.getPlacesDurationMatrix(request);
    }
}

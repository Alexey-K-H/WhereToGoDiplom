package ru.nsu.fit.wheretogo.invoker;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.wheretogo.dto.route.ORSDirectionsDto;
import ru.nsu.fit.wheretogo.dto.route.model.DirectionRequest;
import ru.nsu.fit.wheretogo.dto.route.model.HealthCheck;

import java.util.List;

@Profile("prod")
@Component
public class OpenRouteServiceInvokerImp implements OpenRouteServiceInvoker {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ORS_BASE_URL = "http://ors-app:8080";
    private static final String HEALTH_CHECK = "/ors/v2/health";
    private static final String NOT_READY_STR = "not ready";
    private static final String DIRECTION_DRIVING_CAR_URL = "/ors/v2/directions/driving-car/geojson";
    private static final String DIRECTION_WALKING_URL = "/ors/v2/directions/foot-walking/geojson";

    @Override
    public String health() {
        var url = ORS_BASE_URL + HEALTH_CHECK;
        var result = restTemplate.exchange(url, HttpMethod.GET, null, HealthCheck.class);

        if (result.getBody() != null) {
            return result.getBody().getStatus();
        }

        return NOT_READY_STR;
    }

    @Override
    public ORSDirectionsDto getDirectionDriving() {
        var url = ORS_BASE_URL + DIRECTION_DRIVING_CAR_URL;
        var request = new DirectionRequest(List.of(List.of("83.62234", "54.56074"), List.of("82.71999","54.97187")));

        var result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                ORSDirectionsDto.class);

        if (result.getStatusCode().equals(HttpStatus.OK) && result.getBody() != null) {
            return result.getBody();
        }

        return null;
    }

    @Override
    public ORSDirectionsDto getDirectionWalking() {
        var url = ORS_BASE_URL + DIRECTION_WALKING_URL;


        return null;
    }

}

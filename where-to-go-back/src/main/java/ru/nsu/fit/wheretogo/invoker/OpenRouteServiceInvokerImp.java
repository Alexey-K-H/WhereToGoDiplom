package ru.nsu.fit.wheretogo.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.wheretogo.dto.route.ORSDirectionResponse;
import ru.nsu.fit.wheretogo.dto.route.model.DirectionRequest;
import ru.nsu.fit.wheretogo.dto.route.model.HealthCheck;
import ru.nsu.fit.wheretogo.dto.route.model.LatLong;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenRouteServiceInvokerImp implements OpenRouteServiceInvoker {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenRouteServiceInvokerImp.class);

    @Value("${ru.nsu.fit.wheretogo.ors.base_url}")
    private String orsBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String HEALTH_CHECK = "/ors/v2/health";
    private static final String NOT_READY_STR = "not ready";
    private static final String DIRECTION_DRIVING_CAR_URL = "/ors/v2/directions/driving-car/geojson";
    private static final String DIRECTION_WALKING_URL = "/ors/v2/directions/foot-walking/geojson";

    @Override
    public String health() {
        LOGGER.debug("Base url:{}", orsBaseUrl);

        var url = orsBaseUrl + HEALTH_CHECK;

        LOGGER.debug("Check ORS health");
        var result = restTemplate.exchange(url, HttpMethod.GET, null, HealthCheck.class);

        if (result.getBody() != null) {
            LOGGER.debug("ORS health:{}", result.getBody().getStatus());
            return result.getBody().getStatus();
        }

        return NOT_READY_STR;
    }

    @Override
    public ORSDirectionResponse getDirectionDriving(List<LatLong> keyPlacesCoordinates) {
        var url = orsBaseUrl + DIRECTION_DRIVING_CAR_URL;

        var keyPoints = fillKeyGeoPoints(keyPlacesCoordinates);

        var request = DirectionRequest
                .builder()
                .coordinates(keyPoints)
                .build();

        LOGGER.debug("Отправка запроса на получение маршрута, тело запроса:{}", request);
        var orsResponse = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                ORSDirectionResponse.class);

        if (orsResponse.getStatusCode().equals(HttpStatus.OK) && orsResponse.getBody() != null) {
            LOGGER.debug("Получен ответ от ORS, тело ответа:{}", orsResponse);
            return orsResponse.getBody();
        }

        return null;
    }

    @Override
    public ORSDirectionResponse getDirectionWalking(List<LatLong> keyPlacesCoordinates) {
        var url = orsBaseUrl + DIRECTION_WALKING_URL;

//        var keyPlacesCoordinates = List.of(List.of("83.62234", "54.56074"), List.of("82.71999", "54.97187"));
        var keyPoints = fillKeyGeoPoints(keyPlacesCoordinates);

        var request = DirectionRequest
                .builder()
                .coordinates(keyPoints)
                .build();

        var result = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request),
                ORSDirectionResponse.class);

        if (result.getStatusCode().equals(HttpStatus.OK) && result.getBody() != null) {
            return result.getBody();
        }

        return null;
    }

    private List<List<String>> fillKeyGeoPoints(List<LatLong> coordinates) {
        var result = new ArrayList<List<String>>();
        for (var pointCoordinates : coordinates) {
            result.add(List.of(pointCoordinates.getLongitude(), pointCoordinates.getLatitude()));
        }

        return result;
    }

}

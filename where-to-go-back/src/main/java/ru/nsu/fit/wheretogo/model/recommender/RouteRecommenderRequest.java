package ru.nsu.fit.wheretogo.model.recommender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import ru.nsu.fit.wheretogo.model.ors.common.OrsMode;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;

@Value
@Builder
public class RouteRecommenderRequest {

    @JsonProperty(required = true)
    Integer timeLimit;

    @JsonProperty(required = true)
    LatLong currentUserLocation;

    @JsonProperty(required = true)
    OrsMode mode;
}

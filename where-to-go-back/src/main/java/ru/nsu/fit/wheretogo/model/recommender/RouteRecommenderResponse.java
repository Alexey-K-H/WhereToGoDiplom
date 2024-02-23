package ru.nsu.fit.wheretogo.model.recommender;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.model.ors.ORSDirectionResponse;

import java.util.List;

@Data
@Builder
public class RouteRecommenderResponse {

    @JsonProperty(required = true)
    private ORSDirectionResponse direction;

    @JsonProperty(required = true)
    private List<PlaceBriefDTO> routePlaces;
}

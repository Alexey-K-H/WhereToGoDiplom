package ru.nsu.fit.wheretogo.dto.route;

import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

import java.util.List;

@Data
@Builder
public class RouteRecommendResponse {
    private ORSDirectionResponse direction;
    private List<PlaceBriefDTO> routePlaces;
}

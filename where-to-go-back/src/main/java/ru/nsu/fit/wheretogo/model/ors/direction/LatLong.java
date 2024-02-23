package ru.nsu.fit.wheretogo.model.ors.direction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatLong {

    @JsonProperty(required = true)
    private String latitude;

    @JsonProperty(required = true)
    private String longitude;
}

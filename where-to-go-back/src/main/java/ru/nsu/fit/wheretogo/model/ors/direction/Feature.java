package ru.nsu.fit.wheretogo.model.ors.direction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Feature {

    @JsonProperty(required = true)
    private Properties properties;

    @JsonProperty(required = true)
    private Geometry geometry;
}

package ru.nsu.fit.wheretogo.model.ors.direction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Summary {

    @JsonProperty(required = true)
    private String distance;

    @JsonProperty(required = true)
    private String duration;
}

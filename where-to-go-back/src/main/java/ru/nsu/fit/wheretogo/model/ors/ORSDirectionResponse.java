package ru.nsu.fit.wheretogo.model.ors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.nsu.fit.wheretogo.model.ors.direction.Feature;

import java.util.List;

@Data
public class ORSDirectionResponse {

    @JsonProperty(required = true)
    private List<Feature> features;
}

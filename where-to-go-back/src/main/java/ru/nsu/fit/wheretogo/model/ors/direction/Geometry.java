package ru.nsu.fit.wheretogo.model.ors.direction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Geometry {

    @JsonProperty(required = true)
    private List<List<String>> coordinates;
}

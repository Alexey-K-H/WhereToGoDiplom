package ru.nsu.fit.wheretogo.model.ors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ORSMatrixResponse {

    @JsonProperty(required = true)
    private List<List<String>> durations;
}

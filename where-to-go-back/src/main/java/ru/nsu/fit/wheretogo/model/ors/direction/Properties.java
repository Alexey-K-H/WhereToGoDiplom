package ru.nsu.fit.wheretogo.model.ors.direction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Properties {

    @JsonProperty(required = true)
    private Summary summary;
}

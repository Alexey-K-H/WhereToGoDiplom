package ru.nsu.fit.wheretogo.model.ors.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HealthCheck {

    @JsonProperty(required = true)
    private String status;
}

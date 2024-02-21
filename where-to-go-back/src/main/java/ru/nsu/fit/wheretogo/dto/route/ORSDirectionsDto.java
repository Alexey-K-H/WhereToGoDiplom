package ru.nsu.fit.wheretogo.dto.route;

import lombok.Data;
import ru.nsu.fit.wheretogo.dto.route.model.Feature;

import java.util.List;

@Data
public class ORSDirectionsDto {
    private List<Feature> features;
}

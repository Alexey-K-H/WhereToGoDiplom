package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population;

import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

@Data
@Builder
public class IndividualGene {
    private PlaceBriefDTO placeDescription;
    private double placeAttractionCoefficient;
}

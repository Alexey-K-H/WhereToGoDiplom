package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population;

import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

import java.util.List;

@Data
public class Individual {
    private List<PlaceBriefDTO> routePlaces;
}

package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population;

import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class Individual {
    private List<PlaceBriefDTO> routePlaces = new ArrayList<>();
    private Integer summaryStayDuration = 0;
    private Integer summaryMoveDuration = 0;
    private Double summaryAttractionCoefficient;

    public void addPlace(PlaceBriefDTO place, int moveDuration) {
        this.routePlaces.add(place);
        this.summaryStayDuration += place.getDuration();
        this.summaryMoveDuration += moveDuration;
    }

    public Double calculateAttractionCoefficient() {

        return 0.0;
    }

    public Integer calculateFullDuration() {
        return this.summaryMoveDuration + this.summaryStayDuration;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "routePlaces=" + printRoutePlacesList() +
                ", summaryStayDuration=" + summaryStayDuration +
                ", summaryMoveDuration=" + summaryMoveDuration +
                ", summaryAttractionCoefficient=" + summaryAttractionCoefficient +
                '}';
    }

    private String printRoutePlacesList() {
        var result = new StringBuilder();
        result.append("[");
        for (var place : this.routePlaces) {
            result.append("#").append(place.getId()).append(place.getName()).append(",");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");

        return result.toString();
    }
}

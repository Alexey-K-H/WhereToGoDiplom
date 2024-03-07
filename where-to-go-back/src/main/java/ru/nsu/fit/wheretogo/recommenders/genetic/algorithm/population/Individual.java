package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population;

import lombok.Data;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class Individual {
    private List<IndividualGene> routePlaces = new ArrayList<>();
    private List<Integer> movementsDurations = new ArrayList<>();
    private Integer summaryStayDuration = 0;
    private Integer summaryMoveDuration = 0;
    private Double summaryAttractionCoefficient;

    public void addPlace(PlaceBriefDTO place, int moveDuration, double attractionCoefficient) {
        this.routePlaces.add(IndividualGene
                .builder()
                .placeDescription(place)
                .placeAttractionCoefficient(attractionCoefficient)
                .build()
        );
        this.movementsDurations.add(moveDuration);
        this.summaryStayDuration += place.getDuration();
        this.summaryMoveDuration += moveDuration;
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
            result
                    .append("#")
                    .append(place.getPlaceDescription().getId())
                    .append(" ")
                    .append(place.getPlaceDescription().getName())
                    .append(" ")
                    .append(place.getPlaceAttractionCoefficient())
                    .append(",");
        }
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");

        return result.toString();
    }
}

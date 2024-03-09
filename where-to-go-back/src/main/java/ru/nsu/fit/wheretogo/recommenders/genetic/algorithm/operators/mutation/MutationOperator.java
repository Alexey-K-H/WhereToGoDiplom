package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.mutation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.IndividualGene;
import ru.nsu.fit.wheretogo.service.place.PlaceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MutationOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MutationOperator.class);
    private static final int MILE_MULTIPLIER = 1609;
    private static final Random RANDOM = new Random();

    private final PlaceService placeService;

    @Value("${ru.nsu.fit.wheretogo.genetic.mutation.mutation_probability}")
    private Double mutationProbability;

    @Value("${ru.nsu.fit.wheretogo.genetic.mutation.max_range_for_potential}")
    private Integer maxRangeForPotential;

    public void execute(Individual individual) {

        LOGGER.debug(
                "Запуск оператора мутации.\nПараметры:\nВероятность мутации:{}\nМаксимальный радиус поиска кандидатов:{}",
                mutationProbability, maxRangeForPotential);

        var routePlaceIds = getRoutePlaceIds(individual);

        var index = 0;
        for (IndividualGene individualGene : individual.getRoutePlaces()) {
            var nearPlaceData = checkNearestPlaces(individualGene);

            var nearestPlaces = new ArrayList<PlaceBriefDTO>();
            for (var place : nearPlaceData) {
                if (!routePlaceIds.contains(place.getId())) {
                    nearestPlaces.add(place);
                }
            }

            if (!nearestPlaces.isEmpty() && RANDOM.nextFloat() <= mutationProbability) {
                LOGGER.debug("Найдены точки для замены в маршруте для выполнения мутации");

                var newGene = IndividualGene
                        .builder()
                        .placeDescription(nearestPlaces.get(RANDOM.nextInt(nearestPlaces.size())))
                        .placeAttractionCoefficient(0.0)
                        .build();

                LOGGER.debug("Замена в маршруте: {}", newGene.getPlaceDescription().getName());

                individual.getRoutePlaces().set(index, newGene);

                LOGGER.debug("Потомок после замены: {}", individual);

                break;
            }

            index++;
        }
    }

    private List<Long> getRoutePlaceIds(Individual individual) {
        var routePlaceIds = new ArrayList<Long>();
        for (var individualGene : individual.getRoutePlaces()) {
            routePlaceIds.add(individualGene.getPlaceDescription().getId());
        }
        return routePlaceIds;
    }

    private List<PlaceBriefDTO> checkNearestPlaces(IndividualGene gene) {
        return placeService.getNearestPlacesToPoint(
                gene.getPlaceDescription().getCoordinates().getLatitude().doubleValue(),
                gene.getPlaceDescription().getCoordinates().getLongitude().doubleValue(),
                (double) 10 / MILE_MULTIPLIER,
                (double) maxRangeForPotential / MILE_MULTIPLIER,
                10
        );
    }

}

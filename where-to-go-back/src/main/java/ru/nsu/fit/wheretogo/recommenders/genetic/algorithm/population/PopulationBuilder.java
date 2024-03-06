package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.matrix.DurationMatrix;
import ru.nsu.fit.wheretogo.service.place.PlaceService;
import ru.nsu.fit.wheretogo.utils.helpers.matrix.MatrixHelper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class PopulationBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulationBuilder.class);
    private static final double STAY_TIME_PART = 0.75;
    private static final double MOVE_TIME_PART = 0.25;

    private final PlaceService placeService;

    public List<Individual> buildPopulation(int populationSize, int timeLimit, DurationMatrix durationMatrix) {

        var population = new ArrayList<Individual>();
        initStartLocations(population, populationSize, durationMatrix);
        LOGGER.debug("Начальные позиции в популяции:{}", population);

        fillRoutes(population, timeLimit, durationMatrix);
        LOGGER.debug("Начальная популяция:\n{}", printPopulation(population));

        return population;
    }

    /**
     * Поиск populationSize стартовых позиций
     *
     * @param populationSize размер популяции
     * @param population     хранилище популяции
     */
    private void initStartLocations(List<Individual> population, int populationSize, DurationMatrix durationMatrix) {

        var startMatrixPlacesData = MatrixHelper.getFirstNNearestPlacesToPoint(
                durationMatrix.getMatrix().get(0),
                populationSize,
                List.of(0L)
        );

        for (var matrixPlaceData : startMatrixPlacesData) {
            var placeData = placeService.getPlaceById(matrixPlaceData.getIndex());
            var individual = new Individual();
            individual.addPlace(
                    PlaceBriefDTO.getFromFullDescription(placeData),
                    0
            );

            population.add(individual);
        }
    }

    /**
     * Наполняет пути ближайшими местами до тех пор, пока не будет достигнут предел времени
     *
     * @param population пути в популяции
     * @param timeLimit  временное ограничение
     */
    private void fillRoutes(List<Individual> population, int timeLimit, DurationMatrix durationMatrix) {
        for (var individual : population) {
            var index = 0;
            var usedIndexes = new ArrayList<Long>();
            usedIndexes.add(0L);

            LOGGER.debug("Наполнение маршрута:{}", individual);
            while (individual.calculateFullDuration() <= timeLimit
                    && (double) individual.getSummaryMoveDuration() <= timeLimit * MOVE_TIME_PART
                    && (double) individual.getSummaryStayDuration() <= timeLimit * STAY_TIME_PART) {
                var currPlaceIndex = individual.getRoutePlaces().get(index).getId().intValue();
                LOGGER.debug("Текущая позиция в списке маршрута:{}", currPlaceIndex);
                usedIndexes.add((long) currPlaceIndex);

                var nextNearPlaceMatrixData = MatrixHelper.getFirstNNearestPlacesToPoint(
                        durationMatrix.getMatrix().get(currPlaceIndex),
                        1,
                        usedIndexes
                );

                LOGGER.debug("{}", nextNearPlaceMatrixData);

                var placeData = placeService.getPlaceById(nextNearPlaceMatrixData.get(0).getIndex());

                if ((individual.calculateFullDuration()
                        + (placeData.getDuration() + (nextNearPlaceMatrixData.get(0).getDuration() / 60)))
                        > timeLimit
                        || (individual.getSummaryStayDuration() + placeData.getDuration() > timeLimit * STAY_TIME_PART)
                        || (individual.getSummaryMoveDuration() + ((double) nextNearPlaceMatrixData.get(0).getDuration() / 60) > timeLimit * MOVE_TIME_PART)) {
                    break;
                }

                individual.addPlace(
                        PlaceBriefDTO.getFromFullDescription(placeData),
                        (nextNearPlaceMatrixData.get(0).getDuration() / 60)
                );

                index++;
            }
            LOGGER.debug("Маршрут наполнен:{}\n", individual);
        }
    }

    private String printPopulation(List<Individual> population) {
        var result = new StringBuilder();
        var index = 0;
        for (var individual : population) {
            result.append("Маршрут#").append(index).append(":").append(individual).append("\n");
            index++;
        }
        result.deleteCharAt(result.lastIndexOf("\n"));
        return result.toString();
    }
}

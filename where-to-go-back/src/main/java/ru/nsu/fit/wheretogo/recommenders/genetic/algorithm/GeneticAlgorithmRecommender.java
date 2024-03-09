package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.model.ors.ORSMatrixRequest;
import ru.nsu.fit.wheretogo.model.ors.common.OrsMode;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderRequest;
import ru.nsu.fit.wheretogo.model.recommender.RouteRecommenderResponse;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.function.FitnessFunction;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.matrix.DurationMatrix;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.CrossoverOperator;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.mutation.MutationOperator;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.PopulationBuilder;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.rank.RankFiller;
import ru.nsu.fit.wheretogo.service.openroute.OpenRouteService;
import ru.nsu.fit.wheretogo.service.place.PlaceService;
import ru.nsu.fit.wheretogo.utils.helpers.matrix.MatrixHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class GeneticAlgorithmRecommender {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneticAlgorithmRecommender.class);

    @Value("${ru.nsu.fit.wheretogo.genetic.max_population_size}")
    private Integer maxPopulationSize;

    @Value("${ru.nsu.fit.wheretogo.genetic.max_generations_size}")
    private Integer maxGenerationsSize;

    private final OpenRouteService openRouteService;
    private final PlaceService placeService;

    private final PopulationBuilder populationBuilder;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;
    private final FitnessFunction fitnessFunction;
    private final RankFiller rankFiller;

    public RouteRecommenderResponse execute(RouteRecommenderRequest request) {

        LOGGER.debug("\nПараметры алгоритма:\nРазмер популяции:{}\nМакс. число поколений:{}",
                maxPopulationSize,
                maxGenerationsSize);

        LOGGER.debug("Временное ограничение пользователя:{}", request.getTimeLimit());

        var durationMatrix = new DurationMatrix();
        durationMatrix.setMatrix(buildDurationMatrix(request.getCurrentUserLocation(), request.getMode()));

        LOGGER.debug("Матрица временных затрат:\n{}", MatrixHelper.printDurationsMatrix(durationMatrix));
        LOGGER.debug("Количество мест в матрице:{}", durationMatrix.getMatrix().size());

        var population = populationBuilder.buildPopulation(maxPopulationSize, request.getTimeLimit(), durationMatrix);

        LOGGER.debug("Вычисление рангов маршрутов...");
        rankFiller.updateRoutesAttractionCoefficients(population);
        LOGGER.debug("Популяция с рангами:\n{}", population);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + 15 * 1000;
        for (var generationNumber = 0; generationNumber < 1; generationNumber++) {

            if (System.currentTimeMillis() >= endTime) {
                LOGGER.debug("Время работы алгоритма вышло");
                break;
            }

            LOGGER.debug("Поколение:{}", generationNumber);

            var nextGeneration = new ArrayList<Individual>();

            while (nextGeneration.size() < maxPopulationSize) {
                var nextGenerationIndividual = crossoverOperator.execute(population);

                if (nextGenerationIndividual != null) {
                    mutationOperator.execute(nextGenerationIndividual);
                    nextGeneration.add(nextGenerationIndividual);
                }
            }

            LOGGER.debug("Новое поколение:{}", nextGeneration);

            population = populationUnion(population, nextGeneration);
            LOGGER.debug("Объединение родителей и потомков:{}", population);

            rankFiller.updateRoutesAttractionCoefficients(population);
            LOGGER.debug("Обновление рангов маршрутов:{}", population);

            population = fitnessFunction.selection(population);
            LOGGER.debug("Поколение после селекции:{}", population);
        }

        var bestPath = fitnessFunction.findTheBest(population);
        LOGGER.debug("Лучший путь:{}", bestPath);

        return buildFromIndividual(bestPath);
    }

    private List<List<String>> buildDurationMatrix(LatLong startPosition, OrsMode mode) {

        var locations = buildLocations(startPosition);

        var request = ORSMatrixRequest
                .builder()
                .locations(locations)
                .profile(getProfileName(mode))
                .build();

        return openRouteService.getPlacesDurationMatrix(request).getDurations();
    }

    private List<List<String>> buildLocations(LatLong startPosition) {
        var placesData = placeService.getAllPlaces();

        var result = new ArrayList<List<String>>();

        result.add(0, List.of(startPosition.getLongitude(), startPosition.getLatitude()));

        for (var place : placesData) {
            result.add(
                    place.getId().intValue(),
                    List.of(
                    place.getCoordinates().getLongitude().toString(),
                    place.getCoordinates().getLatitude().toString()));
        }

        return result;
    }

    private String getProfileName(OrsMode mode) {
        switch (mode) {
            case WALKING -> {
                return "foot-walking";
            }
            default -> {
                return "driving-car";
            }
        }
    }

    private RouteRecommenderResponse buildFromIndividual(Individual individual) {
        if (individual != null) {

        }

        return null;
    }

    private List<Individual> populationUnion(List<Individual> population, List<Individual> nextGeneration) {
        return Stream.concat(population.stream(), nextGeneration.stream()).toList();
    }
}

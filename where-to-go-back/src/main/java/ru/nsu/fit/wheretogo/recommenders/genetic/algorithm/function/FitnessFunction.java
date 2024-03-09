package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.ArrayList;
import java.util.List;

@Component
public class FitnessFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(FitnessFunction.class);

    @Value("${ru.nsu.fit.wheretogo.genetic.selection.attraction_coefficient}")
    private Double attractionCoefficient;

    @Value("${ru.nsu.fit.wheretogo.genetic.selection.duration_coefficient}")
    private Double durationCoefficient;

    public List<Individual> selection(List<Individual> population, int limit) {

        LOGGER.debug("Коэффициенты фитнес-функции:{}, {}", attractionCoefficient, durationCoefficient);

        for (var individual : population) {
            individual.setFitnessValue(calculateFitnessValue(individual));
        }

        var result = new ArrayList<>(population);

        LOGGER.debug("Сортировка популяции по фитнес-функции");

        result.sort((o1, o2) -> {
            var diff = o1.getFitnessValue() - o2.getFitnessValue();

            if (diff < 0) {
                return 1;
            }

            if (diff > 0) {
                return -1;
            }

            return 0;
        });

        return result.stream().limit(limit).toList();
    }

    private double calculateFitnessValue(Individual individual) {
        return individual.getSummaryAttractionCoefficient() * attractionCoefficient
                + (individual.getSummaryMoveDuration() + individual.getSummaryStayDuration()) * durationCoefficient;
    }
}

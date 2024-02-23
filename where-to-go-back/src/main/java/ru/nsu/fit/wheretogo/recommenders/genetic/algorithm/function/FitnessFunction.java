package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.Collections;
import java.util.List;

@Component
public class FitnessFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(FitnessFunction.class);

    @Value("${ru.nsu.fit.wheretogo.genetic.attraction_coefficient}")
    private Double attractionCoefficient;

    @Value("${ru.nsu.fit.wheretogo.genetic.duration_coefficient}")
    private Double durationCoefficient;

    public List<Individual> selection(List<Individual> population) {

        LOGGER.debug("Коэффициенты фитнес-функции:{}, {}", attractionCoefficient, durationCoefficient);

        return Collections.emptyList();
    }

    public Individual findTheBest(List<Individual> population) {

        return null;
    }
}

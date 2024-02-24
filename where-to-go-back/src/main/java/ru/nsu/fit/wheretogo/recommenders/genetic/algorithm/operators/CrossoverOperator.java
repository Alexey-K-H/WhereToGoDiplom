package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.Collections;
import java.util.List;

@Component
public class CrossoverOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossoverOperator.class);

    @Value("${ru.nsu.fit.wheretogo.genetic.crossover.time_delta}")
    private Integer deltaTime;

    public List<Individual> execute(List<Individual> population) {

        LOGGER.debug("Запуск оператора скрещивания.\nПараметры\nМаксимальная разность между разрезами:{}", deltaTime);

        return Collections.emptyList();
    }
}

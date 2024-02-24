package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.mutation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.Collections;
import java.util.List;

@Component
public class MutationOperator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MutationOperator.class);

    @Value("${ru.nsu.fit.wheretogo.genetic.mutation.mutation_probability}")
    private Double mutationProbability;

    @Value("${ru.nsu.fit.wheretogo.genetic.mutation.max_range_for_potential}")
    private Integer maxRangeForPotential;

    public List<Individual> execute(List<Individual> population) {

        LOGGER.debug(
                "Запуск оператора мутации.\nПараметры:\nВероятность мутации:{}\nМаксимальный радиус поиска кандидатов:{}",
                mutationProbability, maxRangeForPotential);

        return Collections.emptyList();
    }
}

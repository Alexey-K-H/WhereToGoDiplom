package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.function;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.population.Individual;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FitnessFunction {

    public static List<Individual> selection(List<Individual> population) {

        return Collections.emptyList();
    }

    public static Individual findTheBest(List<Individual> population) {

        return null;
    }
}

package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TimeTag {
    int value;
    int index;
}

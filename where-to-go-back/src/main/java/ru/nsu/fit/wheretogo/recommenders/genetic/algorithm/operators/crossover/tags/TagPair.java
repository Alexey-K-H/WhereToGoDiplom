package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagPair {
    private int firstTag;
    private int secondTag;
    private int diffTags;
}

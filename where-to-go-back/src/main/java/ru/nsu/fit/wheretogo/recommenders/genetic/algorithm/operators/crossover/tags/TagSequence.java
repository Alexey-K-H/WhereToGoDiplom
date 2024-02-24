package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.operators.crossover.tags;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagSequence {
    private final List<TimeTag> tags = new ArrayList<>();

    public void addTag(TimeTag tag) {
        this.tags.add(tag);
    }
}

package ru.nsu.fit.wheretogo.recommenders.genetic.algorithm.matrix;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationMatrix {
    @Getter
    @Setter
    private static List<List<String>> matrix = null;
}

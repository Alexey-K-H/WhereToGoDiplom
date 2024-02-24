package ru.nsu.fit.wheretogo.utils.helpers.matrix;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MatrixPlaceData {
    long index;
    int duration;
}

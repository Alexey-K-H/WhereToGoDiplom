package ru.nsu.fit.wheretogo.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatLongSequences {
    private String latitudes;
    private String longitudes;
}

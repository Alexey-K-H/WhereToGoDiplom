package ru.nsu.fit.wheretogo.dto.route.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatLong {
    private String latitude;
    private String longitude;
}

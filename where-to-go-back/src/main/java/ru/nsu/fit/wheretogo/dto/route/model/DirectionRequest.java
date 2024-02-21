package ru.nsu.fit.wheretogo.dto.route.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DirectionRequest {
    List<List<String>> coordinates;
}

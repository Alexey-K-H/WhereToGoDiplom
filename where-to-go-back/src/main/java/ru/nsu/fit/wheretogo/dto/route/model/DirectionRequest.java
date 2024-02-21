package ru.nsu.fit.wheretogo.dto.route.model;

import lombok.Value;

import java.util.List;

@Value
public class DirectionRequest {
    List<List<String>> coordinates;
}

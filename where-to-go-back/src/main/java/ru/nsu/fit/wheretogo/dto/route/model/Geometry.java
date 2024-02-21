package ru.nsu.fit.wheretogo.dto.route.model;

import lombok.Data;

import java.util.List;

@Data
public class Geometry {
    private List<List<String>> coordinates;
}

package ru.nsu.fit.wheretogo.service.fetch;

import lombok.Getter;

public enum PlaceSortBy {
    NAME("name"),
    SCORE("averageScore")
    ;

    @Getter
    private final String fieldName;

    PlaceSortBy(String fieldName)
    {
        this.fieldName = fieldName;
    }
}

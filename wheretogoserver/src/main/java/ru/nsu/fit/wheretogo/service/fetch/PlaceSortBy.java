package ru.nsu.fit.wheretogo.service.fetch;

import lombok.Getter;

public enum PlaceSortBy {
    NAME("name");

    @Getter
    private final String fieldName;

    PlaceSortBy(String fieldName)
    {
        this.fieldName = fieldName;
    }
}

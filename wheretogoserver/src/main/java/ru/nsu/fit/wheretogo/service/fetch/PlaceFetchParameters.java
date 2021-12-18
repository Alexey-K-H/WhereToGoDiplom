package ru.nsu.fit.wheretogo.service.fetch;

import ru.nsu.fit.wheretogo.utils.SortDirection;

import java.util.List;

public record PlaceFetchParameters(String name,
                                   List<Integer> categoryIds,
                                   Integer page,
                                   Integer pageSize,
                                   SortDirection sortDirection,
                                   PlaceSortBy sortBy
                                   ) {}
//класс-запись для неизменяемых данных, здесь - параметры запроса для where, like и тд
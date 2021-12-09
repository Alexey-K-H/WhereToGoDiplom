package ru.nsu.fit.wheretogo.service.fetch;

import ru.nsu.fit.wheretogo.utils.SortDirection;

public record PlaceFetchParameters(String name,
                                   Integer categoryId,
                                   Integer page,
                                   Integer pageSize,
                                   SortDirection sortDirection,
                                   PlaceSortBy sortBy
                                   ) {}
//класс-запись для неизменяемых данных, здесь - параметры запроса для where, like и тд
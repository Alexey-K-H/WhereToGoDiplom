package ru.nsu.fit.wheretogo.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PagedListDTO<T extends BaseDTO> {

    private List<T> list;//список мест
    private int totalPages;//количество страниц всего
    private int pageNum;////номер страницы, на которой находится место
}

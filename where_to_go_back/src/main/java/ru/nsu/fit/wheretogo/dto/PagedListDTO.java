package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

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

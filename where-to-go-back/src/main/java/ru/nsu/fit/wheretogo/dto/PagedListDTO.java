package ru.nsu.fit.wheretogo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PagedListDTO<T extends BaseDTO> {

    private List<T> list;
    private int totalPages;
    private int pageNum;

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

}

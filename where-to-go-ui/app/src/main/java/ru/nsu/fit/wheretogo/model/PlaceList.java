package ru.nsu.fit.wheretogo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.nsu.fit.wheretogo.model.entity.Place;

public class PlaceList {
    @SerializedName("list")
    @Expose
    private List<Place> list;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("pageNum")
    @Expose
    private Integer pageNum;

    public PlaceList(List<Place> list, Integer totalPages, Integer pageNum) {
        this.list = list;
        this.totalPages = totalPages;
        this.pageNum = pageNum;
    }

    public List<Place> getList() {
        return list;
    }

    public void setList(List<Place> list) {
        this.list = list;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

}

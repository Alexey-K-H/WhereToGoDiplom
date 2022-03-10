package ru.nsu.fit.wheretogo.entity.score;


import java.io.Serializable;
import java.util.Objects;

public class ScoreId implements Serializable {
    private Long author;
    private Long place;

    public ScoreId(Long id, Long userId, Long placeId){
        this.author = userId;
        this.place = placeId;
    }

    public ScoreId(){}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ScoreId s)){
            return false;
        }
        return Objects.equals(author, s.author)
                && Objects.equals(place, s.place);
    }


    public Long getAuthor() {
        return author;
    }

    public Long getPlace() {
        return place;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public void setPlace(Long place) {
        this.place = place;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthor(), getPlace());
    }
}

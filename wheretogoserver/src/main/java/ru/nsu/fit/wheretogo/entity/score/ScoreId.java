package ru.nsu.fit.wheretogo.entity.score;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class ScoreId implements Serializable {

    @Column(name = "user_id")
    private Long author;

    @Column(name = "place_id")
    private Long place;

    public ScoreId(Long userId, Long placeId){
        this.author = userId;
        this.place = placeId;
    }

    public ScoreId(){}
}

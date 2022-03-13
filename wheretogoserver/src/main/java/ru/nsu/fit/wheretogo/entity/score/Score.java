package ru.nsu.fit.wheretogo.entity.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "score")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Score {
    @EmbeddedId
    ScoreId id;

    @ManyToOne
    @MapsId("author")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("place")
    @JoinColumn(name = "place_id")
    Place place;

    @Column(name = "score_value")
    private Integer score;

    public static Score getFromDto(ScoreDTO dto){
        if(dto == null){
            return null;
        }
        return new Score()
                .setId(new ScoreId().setAuthor(dto.getAuthor()).setPlace(dto.getPlace()))
                .setUser(new User().setId(dto.getAuthor()))
                .setPlace(new Place().setId(dto.getPlace()))
                .setScore(dto.getScore());
    }
}

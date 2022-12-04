package ru.nsu.fit.wheretogo.entity.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "score")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Score {
    @EmbeddedId
    ScoreId id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("user")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("place")
    @JoinColumn(name = "place_id")
    Place place;

    @Column(name = "score_value")
    private Integer score;

    public static Score getFromDto(ScoreDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Score()
                .setId(new ScoreId().setUser(dto.getAuthor()).setPlace(dto.getPlace()))
                .setUser(new User().setId(dto.getAuthor()))
                .setPlace(new Place().setId(dto.getPlace()))
                .setScore(dto.getScore());
    }
}

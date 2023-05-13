package ru.nsu.fit.wheretogo.entity.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.nsu.fit.wheretogo.dto.user.ScoreDTO;
import ru.nsu.fit.wheretogo.entity.place.Place;
import ru.nsu.fit.wheretogo.entity.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "T_SCORE")
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
    @JoinColumn(name = "USER_ID")
    User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("place")
    @JoinColumn(name = "PLACE_ID")
    Place place;

    @Column(name = "SCORE_VALUE")
    private Integer scoreValue;

    public static Score getFromDto(ScoreDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Score()
                .setId(new ScoreId().setUser(dto.getAuthor()).setPlace(dto.getPlace()))
                .setUser(new User().setId(dto.getAuthor()))
                .setPlace(new Place().setId(dto.getPlace()))
                .setScoreValue(dto.getScore());
    }
}

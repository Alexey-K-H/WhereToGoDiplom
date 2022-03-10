package ru.nsu.fit.wheretogo.entity.score;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;

import javax.persistence.*;

@Entity
@Table(name = "score")
@IdClass(ScoreId.class)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Score {
    @Id
    @Column(name = "user_id")
    private Long author;

    @Id
    @Column(name = "place_id")
    private Long place;


    @Column(name = "score")
    private Integer score;

    public static Score getFromDto(ScoreDTO dto){
        if(dto == null){
            return null;
        }
        return new Score()
                .setAuthor(dto.getAuthor())
                .setPlace(dto.getPlace())
                .setScore(dto.getScore());
    }
}

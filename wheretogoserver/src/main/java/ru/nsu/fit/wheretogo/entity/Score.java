package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;

import javax.persistence.*;

@Entity
@Table(name = "score")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "score")
    private Integer score;

    public static Score getFromDto(ScoreDTO dto){
        if(dto == null){
            return null;
        }
        return new Score()
                .setId(dto.getId())
                .setAuthor(User.getFromDTO(dto.getAuthor()))
                .setPlace(Place.getFromDTO(dto.getPlace()))
                .setScore(dto.getScore());
    }
}

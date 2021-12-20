package ru.nsu.fit.wheretogo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.dto.ReviewDto;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Review {

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

    @Column(name = "text")
    private String text;

    @Column(name = "written_at")
    private Instant writtenAt;
    
    public static Review getFromDto(ReviewDto dto) {
        if (dto == null) {
            return null;
        }
        return new Review()
                .setId(dto.getId())
                .setAuthor(User.getFromDTO(dto.getAuthor()))
                .setPlace(Place.getFromDTO(dto.getPlace()))
                .setScore(dto.getScore())
                .setText(dto.getText())
                .setWrittenAt(dto.getWrittenAt());
    }
}

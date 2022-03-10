package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.score.Score;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO extends BaseDTO{

    private Long author;
    private Long place;

    @Min(value = 1, message = "Minimum score for a place is 1.")
    @Max(value = 5, message = "Maximum score for a place is 5.")
    private Integer score;

    public static ScoreDTO getFromEntity(Score score){
        if(score == null){
            return null;
        }
        return new ScoreDTO()
                .setAuthor(score.getAuthor())
                .setPlace(score.getPlace())
                .setScore(score.getScore());
    }
}

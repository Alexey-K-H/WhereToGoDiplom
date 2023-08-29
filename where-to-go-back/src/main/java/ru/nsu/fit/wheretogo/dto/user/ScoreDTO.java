package ru.nsu.fit.wheretogo.dto.user;

import lombok.Getter;
import ru.nsu.fit.wheretogo.dto.BaseDTO;
import ru.nsu.fit.wheretogo.entity.score.Score;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class ScoreDTO extends BaseDTO {

    private Long author;
    private Long place;

    @Min(value = 1, message = "Minimum score for a place is 1.")
    @Max(value = 5, message = "Maximum score for a place is 5.")
    private Integer score;

    public static ScoreDTO getFromEntity(Score score) {
        if (score == null) {
            return null;
        }

        var dto = new ScoreDTO();
        dto.setAuthor(score.getUser().getId());
        dto.setPlace(score.getPlace().getId());
        dto.setScore(score.getScoreValue());

        return dto;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public void setPlace(Long place) {
        this.place = place;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
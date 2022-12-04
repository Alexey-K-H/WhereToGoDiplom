package ru.nsu.fit.wheretogo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.Review;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO extends BaseDTO {

    private Long id;
    private UserDTO author;
    @Size(max = 500, message = "Place review must be up to 500 characters.")
    private String text;
    @Min(value = 1, message = "Minimum score for a place is 1.")
    @Max(value = 5, message = "Maximum score for a place is 5.")
    private Integer score;
    private PlaceDescriptionDTO place;
    private Instant writtenAt;

    public static ReviewDTO getFromEntity(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDTO()
                .setId(review.getId())
                .setAuthor(UserDTO.getFromEntity(review.getAuthor()))
                .setPlace(PlaceDescriptionDTO.getFromEntity(review.getPlace()))
                .setScore(review.getScore())
                .setText(review.getText())
                .setWrittenAt(review.getWrittenAt());
    }
}

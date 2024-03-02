package ru.nsu.fit.wheretogo.entity.score;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class ScoreId implements Serializable {

    @Column(name = "user_id")
    private Long user;

    @Column(name = "place_id")
    private Long place;

    public ScoreId(Long userId, Long placeId) {
        this.user = userId;
        this.place = placeId;
    }

    public ScoreId() {
    }

    @Override
    public int hashCode() {
        var result = 17;
        if (user != null) {
            result = 31 * result + user.hashCode();
        }
        if (place != null) {
            result = 31 * result + place.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ScoreId other)) {
            return false;
        }

        return Objects.equals(this.user, other.user) && Objects.equals(this.place, other.place);
    }
}

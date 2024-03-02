package ru.nsu.fit.wheretogo.entity.user.coefficient;

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
public class UserCoefficientId implements Serializable {

    @Column(name = "USER_ID")
    private Long user;

    @Column(name = "CATEGORY_ID")
    private Long category;

    @Override
    public int hashCode() {
        var result = 17;
        if (user != null) {
            result = 31 * result + user.hashCode();
        }
        if (category != null) {
            result = 31 * result + category.hashCode();
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

        if (!(obj instanceof UserCoefficientId other)) {
            return false;
        }

        return Objects.equals(this.user, other.user) && Objects.equals(this.category, other.category);
    }
}

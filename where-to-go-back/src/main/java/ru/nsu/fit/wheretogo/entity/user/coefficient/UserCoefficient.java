package ru.nsu.fit.wheretogo.entity.user.coefficient;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.user.User;

@Entity
@Table(name = "T_USER_COEFFICIENT")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserCoefficient {
    @EmbeddedId
    UserCoefficientId coefficientId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    @MapsId("user")
    private User user;

    @OneToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @MapsId("category")
    private Category category;

    @Column(name = "COEFFICIENT", nullable = false)
    private Double coefficient;

    @Column(name = "COUNT_PLACES", nullable = false)
    private Long placesCount;

}

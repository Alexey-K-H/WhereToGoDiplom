package ru.nsu.fit.wheretogo.entity.user.coefficient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.entity.user.User;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

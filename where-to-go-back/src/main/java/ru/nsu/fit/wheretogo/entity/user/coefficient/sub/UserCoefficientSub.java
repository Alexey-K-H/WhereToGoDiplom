package ru.nsu.fit.wheretogo.entity.user.coefficient.sub;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.nsu.fit.wheretogo.entity.place.Subcategory;
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
@Table(name = "T_USER_COEFFICIENT_SUB")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserCoefficientSub {
    @EmbeddedId
    private UserCoefficientSubId id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    @MapsId("user")
    private User user;

    @OneToOne
    @JoinColumn(name = "SUBCATEGORY_ID", nullable = false)
    @MapsId("subcategory")
    private Subcategory subcategory;

    @Column(name = "COEFFICIENT", nullable = false)
    private Double coefficient;

    @Column(name = "COUNT_PLACES", nullable = false)
    private Long placesCount;
}
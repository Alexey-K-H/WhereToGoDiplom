package ru.nsu.fit.wheretogo.entity.user_coeff;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class UserCoefficientId implements Serializable {

    @Column(name = "user_id")
    private Long user;

    @Column(name = "category_id")
    private Integer category;
}

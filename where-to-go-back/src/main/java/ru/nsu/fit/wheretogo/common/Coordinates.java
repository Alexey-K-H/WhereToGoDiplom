package ru.nsu.fit.wheretogo.common;

import lombok.Data;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
public class Coordinates {
    private BigDecimal latitude;
    private BigDecimal longitude;
}

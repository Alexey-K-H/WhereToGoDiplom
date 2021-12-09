package ru.nsu.fit.wheretogo.common;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
public class Coords {
    private BigDecimal latitude;
    private BigDecimal longitude;
}

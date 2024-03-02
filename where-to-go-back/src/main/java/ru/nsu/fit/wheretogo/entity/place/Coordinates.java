package ru.nsu.fit.wheretogo.entity.place;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Embeddable
@Data
public class Coordinates {
    private BigDecimal latitude;
    private BigDecimal longitude;
}

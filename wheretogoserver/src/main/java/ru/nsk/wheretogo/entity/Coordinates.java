package ru.nsk.wheretogo.entity;
import lombok.*;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable //для встраивания в places
@Data
public class Coordinates {
    private BigDecimal lat;
    private BigDecimal longg;
}

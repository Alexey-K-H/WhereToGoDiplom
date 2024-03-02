package ru.nsu.fit.wheretogo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Базовая модель DTO
 */
@Getter
@Setter
public abstract class BaseDTO {

    @NotNull
    protected Long id;

}

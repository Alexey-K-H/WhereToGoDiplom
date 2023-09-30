package ru.nsu.fit.wheretogo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Базовая модель DTO
 */
@Getter
@Setter
public abstract class BaseDTO {

    @NotNull
    protected Long id;

}

package ru.nsu.fit.wheretogo.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Базовая модель DTO
 */
@Getter
public abstract class BaseDTO {

    @NotNull
    protected Long id;

    public void setId(Long id) {
        this.id = id;
    }

}

package com.libreria.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CalificacionDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer valor;

    public CalificacionDTO() {
    }

    public CalificacionDTO(Integer valor) {
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
package com.libreria.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ResenaDTO {

    private Long id;

    @NotBlank
    private String nombreUsuario;

    @NotBlank
    private String texto;

    public ResenaDTO() {
    }

    public ResenaDTO(Long id, String nombreUsuario, String texto) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.texto = texto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
package com.wedding.api.dto;

public class ConfirmationResponseDto {

    private final boolean confirmada;
    private final String mensaje;

    public ConfirmationResponseDto(boolean confirmada, String mensaje) {
        this.confirmada = confirmada;
        this.mensaje = mensaje;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public String getMensaje() {
        return mensaje;
    }
}

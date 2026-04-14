package com.wedding.api.dto;

public class InvitadoLookupResponseDto {

    private boolean invitado;
    private String nombre;
    private String apellido;

    public InvitadoLookupResponseDto(boolean invitado, String nombre, String apellido) {
        this.invitado = invitado;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public boolean isInvitado() {
        return invitado;
    }

    public void setInvitado(boolean invitado) {
        this.invitado = invitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}

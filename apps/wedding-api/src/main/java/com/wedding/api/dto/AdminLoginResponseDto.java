package com.wedding.api.dto;

public class AdminLoginResponseDto {

    private final String token;
    private final String nombre;
    private final String rol;

    public AdminLoginResponseDto(String token, String nombre, String rol) {
        this.token = token;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }
}

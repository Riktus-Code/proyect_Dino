package com.wedding.api.dto;

public class ConfirmationRequestDto {

	private String nombre;
	private String apellido;
	private String alergias;
	private Boolean acompanante;

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

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public Boolean getAcompanante() {
		return acompanante;
	}

	public void setAcompanante(Boolean acompanante) {
		this.acompanante = acompanante;
	}
}

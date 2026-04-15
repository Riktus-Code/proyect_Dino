package com.wedding.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmationRequestDto {

	private String nombre;
	private String apellido;
	private String alergias;
	private Boolean acompanante;
}

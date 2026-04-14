package com.wedding.api.controller;

import com.wedding.api.dto.ConfirmationRequestDto;
import com.wedding.api.dto.ConfirmationResponseDto;
import com.wedding.api.dto.WeddingDetailsDto;
import com.wedding.api.service.ConfirmacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"}, methods = {
        org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.POST
})
public class WeddingController {

    private final ConfirmacionService confirmacionService;

    public WeddingController(ConfirmacionService confirmacionService) {
        this.confirmacionService = confirmacionService;
    }

    @GetMapping("/detalles-boda")
    public ResponseEntity<WeddingDetailsDto> getDetallesBoda() {
        return ResponseEntity.ok(new WeddingDetailsDto());
    }

    @PostMapping("/confirmacion")
    public ResponseEntity<ConfirmationResponseDto> enviarConfirmacion(@RequestBody ConfirmationRequestDto request) {
        try {
            ConfirmationResponseDto response = confirmacionService.confirmarAsistencia(
                    request.getNombre(),
                    request.getApellido(),
                    request.getAlergias(),
                    request.getAcompanante()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ConfirmationResponseDto(false, "No hemos encontrado tus datos en la lista de invitados."));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ConfirmationResponseDto(false, "Ya existe una confirmación previa para este usuario."));
        }
    }
}

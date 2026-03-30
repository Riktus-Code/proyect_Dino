package com.wedding.api.controller;

import com.wedding.api.dto.ConfirmationRequestDto;
import com.wedding.api.dto.WeddingDetailsDto;
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

    @GetMapping("/detalles-boda")
    public ResponseEntity<WeddingDetailsDto> getDetallesBoda() {
        return ResponseEntity.ok(new WeddingDetailsDto());
    }

    @PostMapping("/confirmacion")
    public ResponseEntity<Void> enviarConfirmacion(@RequestBody ConfirmationRequestDto request) {
        return ResponseEntity.ok().build();
    }
}

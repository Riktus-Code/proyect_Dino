package com.wedding.api.controller;

import com.wedding.api.dto.AdminLoginRequestDto;
import com.wedding.api.dto.AdminLoginResponseDto;
import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.UsuarioRepository;
import com.wedding.api.service.AdminAuthService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final AdminAuthService adminAuthService;

    public AuthController(UsuarioRepository usuarioRepository, AdminAuthService adminAuthService) {
        this.usuarioRepository = usuarioRepository;
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AdminLoginResponseDto> loginAdmin(@RequestBody AdminLoginRequestDto request) {
        Optional<Usuario> usuario = usuarioRepository.findFirstByNombreAndPassword(request.getNombre(), request.getPassword());

        if (usuario.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        String token = adminAuthService.createToken(usuario.get());
        AdminLoginResponseDto response = new AdminLoginResponseDto(token, usuario.get().getNombre(), usuario.get().getRol());
        return ResponseEntity.ok(response);
    }
}

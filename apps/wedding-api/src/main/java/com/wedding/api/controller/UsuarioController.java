package com.wedding.api.controller;

import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.UsuarioRepository;
import com.wedding.api.service.AdminAuthService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"}, methods = {
        org.springframework.web.bind.annotation.RequestMethod.GET,
        org.springframework.web.bind.annotation.RequestMethod.POST,
        org.springframework.web.bind.annotation.RequestMethod.PUT,
        org.springframework.web.bind.annotation.RequestMethod.DELETE,
        org.springframework.web.bind.annotation.RequestMethod.OPTIONS
})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final AdminAuthService adminAuthService;

    public UsuarioController(UsuarioRepository usuarioRepository, AdminAuthService adminAuthService) {
        this.usuarioRepository = usuarioRepository;
        this.adminAuthService = adminAuthService;
    }

    private boolean isUnauthorized(String token) {
        return !adminAuthService.isValidAdminToken(token);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (isUnauthorized(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (isUnauthorized(token)) {
            return ResponseEntity.status(401).build();
        }
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @RequestBody Usuario usuario,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (isUnauthorized(token)) {
            return ResponseEntity.status(401).build();
        }
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            usuario.setPassword("Temporal123*");
        }
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioRequest,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (isUnauthorized(token)) {
            return ResponseEntity.status(401).build();
        }
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setApellido(usuarioRequest.getApellido());
        usuario.setCorreo(usuarioRequest.getCorreo());

        if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isBlank()) {
            usuario.setPassword(usuarioRequest.getPassword());
        }

        if (usuarioRequest.getRol() != null && !usuarioRequest.getRol().isBlank()) {
            usuario.setRol(usuarioRequest.getRol());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @PathVariable Long id,
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (isUnauthorized(token)) {
            return ResponseEntity.status(401).build();
        }
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
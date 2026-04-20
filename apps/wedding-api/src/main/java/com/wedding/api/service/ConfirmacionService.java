package com.wedding.api.service;

import com.wedding.api.dto.ConfirmationResponseDto;
import com.wedding.api.entity.Confirmacion;
import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.ConfirmacionRepository;
import com.wedding.api.repository.UsuarioRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ConfirmacionService {

    private final UsuarioRepository usuarioRepository;
    private final ConfirmacionRepository confirmacionRepository;

    public ConfirmacionService(
            UsuarioRepository usuarioRepository,
            ConfirmacionRepository confirmacionRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.confirmacionRepository = confirmacionRepository;
    }

    @Transactional
    public ConfirmationResponseDto confirmarAsistencia(
            String nombre,
            String apellido,
            String alergias,
            Boolean acompanante
    ) {
        String nombreLimpio = limpiarTexto(nombre);
        String apellidoLimpio = limpiarTexto(apellido);

        Usuario usuario = usuarioRepository
            .findInvitadoByNombreApellido(nombreLimpio, apellidoLimpio)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (confirmacionRepository.existsByUsuarioId(usuario.getId())) {
            throw new IllegalStateException("El usuario ya confirmó asistencia");
        }

        Confirmacion confirmacion = new Confirmacion();
        confirmacion.setUsuario(usuario);
        confirmacion.setAcompanante(Boolean.TRUE.equals(acompanante));
        confirmacion.setAlergias(StringUtils.hasText(alergias) ? alergias.trim() : "Sin alergias ni intolerancias");
        confirmacion.setFechaConfirmacion(LocalDateTime.now());
        confirmacionRepository.save(confirmacion);

        // SMTP pausado temporalmente: por ahora solo se guarda la confirmacion en base de datos.
        // enviarCorreoConfirmacion(usuario, confirmacion);

        String mensajeRespuesta = Boolean.TRUE.equals(acompanante)
                ? "Confirmación guardada. El usuario asistirá acompañado."
                : "Confirmación guardada. El usuario asistirá sin acompañante.";

        return new ConfirmationResponseDto(true, mensajeRespuesta);
    }

    private String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }
}

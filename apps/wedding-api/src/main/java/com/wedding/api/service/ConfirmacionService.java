package com.wedding.api.service;

import com.wedding.api.dto.ConfirmationResponseDto;
import com.wedding.api.entity.Confirmacion;
import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.ConfirmacionRepository;
import com.wedding.api.repository.UsuarioRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ConfirmacionService {

    private static final String CONFIRMATION_EMAIL = "manolito6989@gmail.com";

    private static final Logger log = LoggerFactory.getLogger(ConfirmacionService.class);

    private final UsuarioRepository usuarioRepository;
    private final ConfirmacionRepository confirmacionRepository;
    private final JavaMailSender mailSender;

    public ConfirmacionService(
            UsuarioRepository usuarioRepository,
            ConfirmacionRepository confirmacionRepository,
            JavaMailSender mailSender
    ) {
        this.usuarioRepository = usuarioRepository;
        this.confirmacionRepository = confirmacionRepository;
        this.mailSender = mailSender;
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
                .findFirstByNombreIgnoreCaseAndApellidoIgnoreCase(nombreLimpio, apellidoLimpio)
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

        enviarCorreoConfirmacion(usuario, confirmacion);

        String mensajeRespuesta = Boolean.TRUE.equals(acompanante)
                ? "Confirmación guardada. El usuario asistirá acompañado."
                : "Confirmación guardada. El usuario asistirá sin acompañante.";

        return new ConfirmationResponseDto(true, mensajeRespuesta);
    }

    private void enviarCorreoConfirmacion(Usuario usuario, Confirmacion confirmacion) {
        String mensaje;
        if (confirmacion.isAcompanante()) {
            mensaje = "La persona " + usuario.getNombre() + " " + usuario.getApellido()
                    + " ha confirmado su asistencia y asistirá acompañado.";
        } else {
            mensaje = "La persona " + usuario.getNombre() + " " + usuario.getApellido()
                    + " ha confirmado su asistencia y no asistirá acompañado.";
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(CONFIRMATION_EMAIL);
        mail.setTo(CONFIRMATION_EMAIL);
        mail.setSubject("Nueva confirmación de asistencia");
        mail.setText(mensaje + "\nAlergias / intolerancias: " + confirmacion.getAlergias());

        try {
            mailSender.send(mail);
        } catch (MailException ex) {
            log.warn("No se pudo enviar el correo de confirmación: {}", ex.getMessage());
        }
    }

    private String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }
}

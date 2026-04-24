package com.wedding.api.service;

import com.wedding.api.dto.ConfirmationResponseDto;
import com.wedding.api.entity.Confirmacion;
import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.ConfirmacionRepository;
import com.wedding.api.repository.UsuarioRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ConfirmacionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmacionService.class);

    private final UsuarioRepository usuarioRepository;
    private final ConfirmacionRepository confirmacionRepository;
    private final JavaMailSender mailSender;

    @Value("${app.mail.to:${spring.mail.username}}")
    private String correoDestino;

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

        enviarCorreoConfirmacion(usuario, confirmacion);

        String mensajeRespuesta = Boolean.TRUE.equals(acompanante)
                ? "Confirmación guardada. El usuario asistirá acompañado."
                : "Confirmación guardada. El usuario asistirá sin acompañante.";

        return new ConfirmationResponseDto(true, mensajeRespuesta);
    }

    private String limpiarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private void enviarCorreoConfirmacion(Usuario usuario, Confirmacion confirmacion) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(correoDestino);
            mensaje.setFrom("manolito6989@gmail.com");
            mensaje.setSubject("Confirmación de asistencia");
            mensaje.setText(construirCuerpoCorreo(usuario, confirmacion));
            mailSender.send(mensaje);
        } catch (Exception exception) {
            LOGGER.error("ERROR AL ENVIAR EL CORREO", exception);
            exception.printStackTrace();
        }
    }

    private String construirCuerpoCorreo(Usuario usuario, Confirmacion confirmacion) {
        String estadoAcompanante = Boolean.TRUE.equals(confirmacion.getAcompanante())
                ? "ira acompañado"
                : "no ira acompañado";

        return "El usuario "
                + usuario.getNombre()
                + " "
                + usuario.getApellido()
                + " ha confirmado su asistencia.\n"
                + "Ira acompañado: "
                + estadoAcompanante
                + ".\n"
                + "Tiene los alergenos "
                + confirmacion.getAlergias()
                + ".";
    }
}

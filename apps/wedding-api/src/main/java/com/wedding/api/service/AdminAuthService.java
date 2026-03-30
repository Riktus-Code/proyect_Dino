package com.wedding.api.service;

import com.wedding.api.entity.Usuario;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private static class SessionData {
        private final String nombre;
        private final String rol;

        SessionData(String nombre, String rol) {
            this.nombre = nombre;
            this.rol = rol;
        }
    }

    private final Map<String, SessionData> tokens = new ConcurrentHashMap<>();

    public String createToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new SessionData(usuario.getNombre(), usuario.getRol()));
        return token;
    }

    public boolean isValidAdminToken(String token) {
        if (token == null) {
            return false;
        }

        SessionData sessionData = tokens.get(token);
        return sessionData != null && "ADMIN".equalsIgnoreCase(sessionData.rol);
    }
}

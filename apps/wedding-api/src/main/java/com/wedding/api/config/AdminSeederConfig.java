package com.wedding.api.config;

import com.wedding.api.entity.Usuario;
import com.wedding.api.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminSeederConfig {

    @Bean
    CommandLineRunner seedAdminUser(UsuarioRepository usuarioRepository) {
        return args -> {
            String adminCorreo = "admin@wedding.local";
            Optional<Usuario> existingByCorreo = usuarioRepository.findByCorreo(adminCorreo);

            if (existingByCorreo.isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Wedding");
                admin.setCorreo(adminCorreo);
                admin.setPassword("Admin123*");
                admin.setRol("ADMIN");
                usuarioRepository.save(admin);
            } else {
                Usuario admin = existingByCorreo.get();
                admin.setNombre("Admin");
                admin.setApellido("Wedding");
                admin.setPassword("Admin123*");
                admin.setRol("ADMIN");
                usuarioRepository.save(admin);
            }

            String userCorreo = "juan.perez@wedding.local";
            Optional<Usuario> existingUserByCorreo = usuarioRepository.findByCorreo(userCorreo);

            if (existingUserByCorreo.isEmpty()) {
                Usuario user = new Usuario();
                user.setNombre("Juan");
                user.setApellido("Perez");
                user.setCorreo(userCorreo);
                user.setPassword("User123*");
                user.setRol("USER");
                usuarioRepository.save(user);
            } else {
                Usuario user = existingUserByCorreo.get();
                user.setNombre("Juan");
                user.setApellido("Perez");
                user.setPassword("User123*");
                user.setRol("USER");
                usuarioRepository.save(user);
            }
        };
    }
}

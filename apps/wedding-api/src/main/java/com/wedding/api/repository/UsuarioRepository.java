package com.wedding.api.repository;

import com.wedding.api.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByCorreo(String correo);

	Optional<Usuario> findFirstByNombreAndPassword(String nombre, String password);
}

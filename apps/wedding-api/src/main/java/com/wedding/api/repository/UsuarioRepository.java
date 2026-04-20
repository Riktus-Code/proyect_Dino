package com.wedding.api.repository;

import com.wedding.api.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByCorreo(String correo);

	@Query(value = """
			SELECT *
			FROM usuario u
			WHERE lower(trim(u.nombre)) = lower(trim(:nombre))
			  AND lower(trim(u.apellido)) = lower(trim(:apellido))
			ORDER BY u.id
			LIMIT 1
			""", nativeQuery = true)
	Optional<Usuario> findInvitadoByNombreApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);

	Optional<Usuario> findFirstByNombreAndPassword(String nombre, String password);
}

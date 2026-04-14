package com.wedding.api.repository;

import com.wedding.api.entity.Confirmacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmacionRepository extends JpaRepository<Confirmacion, Long> {

    boolean existsByUsuarioId(Long usuarioId);

    Optional<Confirmacion> findByUsuarioId(Long usuarioId);
}

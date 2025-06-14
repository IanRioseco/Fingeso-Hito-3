package com.example.Backend.Repository;

import com.example.Backend.Entity.farmaceuticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface farmaceuticoRepository extends JpaRepository<farmaceuticoEntity, Long> {
    Optional<farmaceuticoEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<farmaceuticoEntity> findByNombre(String nombre);
    Optional<farmaceuticoEntity> findByRut(String rut);
}

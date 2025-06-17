package com.example.Backend.Repository;

import com.example.Backend.Entity.recepcionistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface recepcionistaRepository extends JpaRepository<recepcionistaEntity, Long> {
    Optional<recepcionistaEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<recepcionistaEntity> findByNombre(String nombre);
    Optional<recepcionistaEntity> findByRut(String rut);
}

package com.example.Backend.Repository;

import com.example.Backend.Entity.pacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface pacienteRepository extends JpaRepository<pacienteEntity, Long> {
    Optional<pacienteEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<pacienteEntity> findByNombrePa(String nombrePa);
    Optional<pacienteEntity> findByRut(String rut);
}

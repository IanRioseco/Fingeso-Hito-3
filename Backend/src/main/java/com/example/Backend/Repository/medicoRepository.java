package com.example.Backend.Repository;

import com.example.Backend.Entity.medicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface medicoRepository extends JpaRepository<medicoEntity, Long> {
    Optional<medicoEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<medicoEntity> findByNombre(String nombre);
    Optional<medicoEntity> findByRut(String rut);
}

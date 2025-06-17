package com.example.Backend.Repository;

import com.example.Backend.Entity.personaltecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface personaltecnicoRepository extends JpaRepository<personaltecnicoEntity, Long> {
    Optional<personaltecnicoEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<personaltecnicoEntity> findByNombre(String nombre);
    Optional<personaltecnicoEntity> findByRut(String rut);
}

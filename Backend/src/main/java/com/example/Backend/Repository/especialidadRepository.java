package com.example.Backend.Repository;

import com.example.Backend.Entity.especialidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface especialidadRepository extends JpaRepository<especialidadEntity, Long> {
    Optional<especialidadEntity> findByNombre(String nombre);
}

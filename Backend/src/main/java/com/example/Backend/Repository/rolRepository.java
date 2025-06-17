package com.example.Backend.Repository;

import com.example.Backend.Entity.rolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface rolRepository extends JpaRepository<rolEntity, Long> {
    Optional<rolEntity> findByNombre(String nombre);
}

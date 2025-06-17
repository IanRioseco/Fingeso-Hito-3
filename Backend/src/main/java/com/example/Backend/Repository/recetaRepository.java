package com.example.Backend.Repository;

import com.example.Backend.Entity.recetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface recetaRepository extends JpaRepository<recetaEntity, Long> {
}

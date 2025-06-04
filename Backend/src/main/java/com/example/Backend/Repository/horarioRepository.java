package com.example.Backend.Repository;

import com.example.Backend.Entity.horarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface horarioRepository extends JpaRepository<horarioEntity, Long> {
}

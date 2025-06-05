package com.example.Backend.Repository;

import com.example.Backend.Entity.pacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface pacienteRepository extends JpaRepository<pacienteEntity, Long> {
}

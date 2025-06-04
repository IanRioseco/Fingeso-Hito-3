package com.example.Backend.Repository;

import com.example.Backend.Entity.farmaceuticoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface farmaceuticoRepository extends JpaRepository<farmaceuticoEntity, Long> {
}

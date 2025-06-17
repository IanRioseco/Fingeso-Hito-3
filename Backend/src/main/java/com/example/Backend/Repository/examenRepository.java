package com.example.Backend.Repository;

import com.example.Backend.Entity.examenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface examenRepository extends JpaRepository<examenEntity, Long> {
}

package com.example.Backend.Repository;

import com.example.Backend.Entity.farmaciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface farmaciaRepository extends JpaRepository<farmaciaEntity, Long> {
}

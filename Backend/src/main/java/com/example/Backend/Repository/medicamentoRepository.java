package com.example.Backend.Repository;

import com.example.Backend.Entity.medicamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface medicamentoRepository extends JpaRepository<medicamentoEntity,Long> {
}

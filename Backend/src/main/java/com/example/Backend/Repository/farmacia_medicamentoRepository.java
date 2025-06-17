package com.example.Backend.Repository;

import com.example.Backend.Entity.farmacia_medicamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface farmacia_medicamentoRepository extends JpaRepository<farmacia_medicamentoEntity, Long> {
    @Query("SELECT fm FROM farmacia_medicamentoEntity fm WHERE fm.farmacia.idFarmacia = :farmaciaId")
    List<farmacia_medicamentoEntity> findByFarmaciaId(@Param("farmaciaId") Long farmaciaId);
}

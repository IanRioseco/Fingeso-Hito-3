package com.example.Backend.Repository;

import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Entity.receta_medicamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface receta_medicamentoRepository extends JpaRepository<receta_medicamentoEntity, Long> {

    @Query("SELECT rm.medicamento FROM receta_medicamentoEntity rm WHERE rm.receta.id_receta = :idReceta")
    List<medicamentoEntity> findMedicamentosByRecetaId(@Param("idReceta") Long idReceta);

    @Query("SELECT DISTINCT rm.receta FROM receta_medicamentoEntity rm " +
            "JOIN rm.medicamento m " +
            "JOIN farmacia_medicamentoEntity fm ON m.idmedicamento = fm.medicamento.idmedicamento " +
            "WHERE fm.farmacia.idFarmacia = :farmaciaId")
    List<recetaEntity> findRecetasByFarmaciaId(@Param("farmaciaId") Long farmaciaId);
}

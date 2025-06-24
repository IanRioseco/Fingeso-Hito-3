package com.example.Backend.Repository;

import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Entity.recetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface recetaRepository extends JpaRepository<recetaEntity, Long> {

    @Query("SELECT r FROM recetaEntity r WHERE r.citamedica.Id_citamedica = :idCita")
    List<recetaEntity> findByCitaId(@Param("idCita") Long idCita);

}


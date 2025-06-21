package com.example.Backend.Repository;

import com.example.Backend.Entity.horarioEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface horarioRepository extends JpaRepository<horarioEntity, Long> {

    List<horarioEntity> findByMedico_Idmedico(Long idMedico);

    @Query("SELECT h FROM horarioEntity h WHERE h.citamedica.Id_citamedica = :idCita")
    List<horarioEntity> findByCitamedicaId(@Param("idCita") Long idCita);

    @Modifying
    @Transactional
    @Query("DELETE FROM horarioEntity h WHERE h.citamedica.Id_citamedica = :idCita")
    void deleteByCitamedicaId(@Param("idCita") Long idCita);
}

package com.example.Backend.Repository;

import com.example.Backend.Entity.citamedicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface citamedicaRepository extends JpaRepository<citamedicaEntity, Long> {
    @Query(value = "SELECT * FROM citas_medicas WHERE id_paciente = :idPaciente", nativeQuery = true)
    List<citamedicaEntity> findByPacienteId(@Param("idPaciente") Long idPaciente);

    @Query("SELECT c FROM citamedicaEntity c WHERE c.medico.idmedico = :idMedico")
    List<citamedicaEntity> findAllCitasMedicas(@Param("idMedico") Long idMedico);
}

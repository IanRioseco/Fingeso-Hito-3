package com.example.Backend.Repository;

import com.example.Backend.Entity.citamedicaEntity;
import com.example.Backend.Entity.medicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface medicoRepository extends JpaRepository<medicoEntity, Long> {
    Optional<medicoEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    Optional<medicoEntity> findByNombre(String nombre);
    Optional<medicoEntity> findByRut(String rut);

    @Query("SELECT m FROM medicoEntity m WHERE m.especialidad.id_especialidad = :idEspecialidad")
    List<medicoEntity> findByEspecialidadId(@Param("idEspecialidad") Long idEspecialidad);

}

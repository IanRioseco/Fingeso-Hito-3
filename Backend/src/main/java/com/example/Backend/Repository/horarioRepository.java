package com.example.Backend.Repository;

import com.example.Backend.Entity.horarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface horarioRepository extends JpaRepository<horarioEntity, Long> {

    List<horarioEntity> findByMedico_Idmedico(Long idMedico);
}

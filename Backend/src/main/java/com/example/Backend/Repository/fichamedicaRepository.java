package com.example.Backend.Repository;

import com.example.Backend.Entity.fichamedicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface fichamedicaRepository extends JpaRepository<fichamedicaEntity, Long>{

    fichamedicaEntity findByPaciente_RutPa(Long rutPa);

}

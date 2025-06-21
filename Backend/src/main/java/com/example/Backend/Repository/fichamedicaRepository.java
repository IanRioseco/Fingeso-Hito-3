package com.example.Backend.Repository;

import com.example.Backend.Entity.fichamedicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface fichamedicaRepository extends JpaRepository<fichamedicaEntity, Long>{

    fichamedicaEntity findByPaciente_RutPa(Long rutPa);

}

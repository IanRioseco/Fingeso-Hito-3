package com.example.Backend.Repository;

import com.example.Backend.Entity.medicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface medicoRepository extends JpaRepository<medicoEntity, Long> {
}

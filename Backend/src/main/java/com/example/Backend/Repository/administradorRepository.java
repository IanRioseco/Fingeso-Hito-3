package com.example.Backend.Repository;

import com.example.Backend.Entity.administradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface administradorRepository extends JpaRepository<administradorEntity, Long> {

    /*Este apartado sirve para hacer consultas mas elavoradas*/
    Optional<administradorEntity> FindByEmail(String email);
}

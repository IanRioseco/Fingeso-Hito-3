package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "especialidades")
public class especialidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_especialidad")
    private Long id_especialidad;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
}

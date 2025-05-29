package com.example.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "especialidad")
public class especialidadEntity {

    @Id
    @Column(name = "Id_especialidad")
    private Long id_especialidad;

    @Column(name = "nombre", length = 100)
    private String nombre;
}

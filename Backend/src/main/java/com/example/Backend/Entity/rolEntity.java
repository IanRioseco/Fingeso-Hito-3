package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class rolEntity {

    @Id
    @Column(name = "Id_rol")
    private Long id_rol;

    @Column(name = "nombre")
    private String nombre;

}

package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "administrador")
public class administradorEntity {

    @Id
    @Column(name = "Id_admin")
    private Long id_admin;

    @Column(name = "Nombre", length = 100)
    private String nombre;

    @Column(name = "Apellido", length = 100)
    private String apellido;

    @OneToOne
    @JoinColumn(name = "Id_especialidad", referencedColumnName = "Id_especialidad")
    private especialidadEntity especialidad;
}

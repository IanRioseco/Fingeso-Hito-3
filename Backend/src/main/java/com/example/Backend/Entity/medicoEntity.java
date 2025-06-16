package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "medicos")
public class medicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_medico")
    private Long idmedico;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "rut", length = 20, nullable = false, unique = true)
    private String rut;

    /*Medico->Especialidad*/
    @ManyToOne
    @JoinColumn(name = "Id_especialidad", referencedColumnName = "Id_especialidad")
    private especialidadEntity especialidad;

    /*Medico->Rol*/
    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private rolEntity rol;


}

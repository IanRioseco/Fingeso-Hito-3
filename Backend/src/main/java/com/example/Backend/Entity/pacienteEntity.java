package com.example.Backend.Entity;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pacientes")
public class pacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_paciente")
    private Long rutPa;

    @Column(name = "nombre_paciente", length = 80, nullable = false)
    private String nombrePa;

    @Column(name = "apellido_paciente", length = 80, nullable = false)
    private String apellidoPa;

    @Column(name = "fechanacimiento")
    private LocalDate fechanacimiento;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo", length = 80)
    private String correo;

    @Column(name = "direccion", length = 80)
    private String direccion;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "rut", length = 20, nullable = false, unique = true)
    private String rut;


    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private rolEntity rol;

}

package com.example.Backend.Entity;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pacientes")
public class pacienteEntity {

    @Id
    @Column(name = "Id_paciente", length = 10)
    private String rutPa;

    @Column(name = "nombre_paciente", length = 80, nullable = false)
    private String nombrePa;

    @Column(name = "apellido_paciente", length = 80, nullable = false)
    private String apellidoPa;

    @Column(name = "telefono")
    private Long telefono;

    @Column(name = "correo", length = 80)
    private String correo;

    @Column(name = "direccion", length = 80)
    private String direccion;

    @OneToMany(mappedBy = "pacientes", cascade = CascadeType.ALL)
    private List<citamedicaEntity> citamedicas = new ArrayList<>();

    @OneToMany(mappedBy = "pacientes", cascade = CascadeType.ALL)
    private List<examenEntity> examenes = new ArrayList<>();

    @OneToMany(mappedBy = "pacientes", cascade = CascadeType.ALL)
    private List<recetaEntity> recetas = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "Id_fichamedica", referencedColumnName = "Id_fichamedica")
    private fichamedicaEntity fichamedica;

}

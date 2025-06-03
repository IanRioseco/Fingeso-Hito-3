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
    @Column(name = "Id_medico")
    private Long id_medico;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    /*Medico->CitaMedica*/
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<citamedicaEntity> citamedicas = new ArrayList<>();

    /*Medico->Horario*/
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<horarioEntity> horarios = new ArrayList<>();

    /*Medico->Especialidad*/
    @OneToOne
    @JoinColumn(name = "Id_especialidad", referencedColumnName = "Id_especialidad")
    private especialidadEntity especialidad;

    /*Medico->Reporte*/
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<reporteEntity> reportes = new ArrayList<>();

    /*Medico->Receta*/
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<recetaEntity> recetas = new ArrayList<>();

    /*Medico->Examen*/
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<examenEntity> examenes = new ArrayList<>();
}

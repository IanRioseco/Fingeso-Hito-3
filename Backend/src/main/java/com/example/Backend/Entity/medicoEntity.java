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
    private Long idmedico;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    /*Medico->Especialidad*/
    @ManyToOne
    @JoinColumn(name = "Id_especialidad", referencedColumnName = "Id_especialidad")
    private especialidadEntity especialidad;


}

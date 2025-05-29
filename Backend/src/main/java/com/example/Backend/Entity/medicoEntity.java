package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicos")
public class medicoEntity {

    @Id
    @Column(name = "Id_medico")
    private Long id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellido", length = 50)
    private String apellido;

    @Column(name = "Correo", length = 50)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    @OneToOne
    @JoinColumn(name = "Id_especialidad", referencedColumnName = "Id_especialidad")
    private especialidadEntity especialidad;
}

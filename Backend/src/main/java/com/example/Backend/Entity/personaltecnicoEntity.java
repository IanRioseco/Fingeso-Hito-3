package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PersonalTecnico")
public class personaltecnicoEntity {

    @Id
    @Column(name = "Id_pertec")
    private Long id_pertec;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "correo", length = 10, nullable = false)
    private String correo;

    @Column(name = "telefono", length = 10)
    private String telefono; //no creo que sea muy necesario el telefono

    /*Personal tecnico -> Rol*/
    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private rolEntity rol;
}

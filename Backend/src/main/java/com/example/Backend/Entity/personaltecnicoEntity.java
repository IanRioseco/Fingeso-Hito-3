package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PersonalTecnico")
public class personaltecnicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_pertec")
    private Long id_pertec;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "correo", length = 100, nullable = false)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono; //no creo que sea muy necesario el telefono

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "rut", length = 20, nullable = false, unique = true)
    private String rut;

    /*Personal tecnico -> Rol*/
    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private rolEntity rol;
    
}

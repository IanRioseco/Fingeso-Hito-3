package com.example.Backend.Entity;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "Farmaceuticos")
public class farmaceuticoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_farmaceutico")
    private Long id_farmaceutico; // Cambié el nombre para seguir un formato similar a administradorEntity

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "Apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "rut", length = 20, nullable = false, unique = true)
    private String rut;

    /*Farmaceutico->Rol*/
    @ManyToOne
    @JoinColumn(name = "Id_rol", referencedColumnName = "Id_rol")
    private rolEntity rol;

    /*Farmaceutico->Farmacia*/
    @ManyToOne
    @JoinColumn(name = "Id_farmacia", referencedColumnName = "Id_farmacia")
    private farmaciaEntity farmacia;
}
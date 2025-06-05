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
    private Long Id_farmaceutico;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    /*Farmaceutico->Farmacia*/
    @ManyToOne
    @JoinColumn(name = "Id_farmacia", referencedColumnName = "Id_farmacia")
    private farmaciaEntity farmacia;

}

package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recepcionistas")
public class recepcionistaEntity {

    @Id
    @Column(name = "ID_recepcionista")
    private Long IdRecepcionista;

    @Column(name = "nombre", length = 50, nullable = false)
    private String Nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String Apellido;

    @Column(name = "correo", length = 20, nullable = false)
    private String Correo;

    @Column(name = "telefono", length = 10)
    private String Telefono;

    @OneToMany(mappedBy = "recepcionistas", cascade = CascadeType.ALL)
    private List<citamedicaEntity> citamedicas = new ArrayList<>();
}

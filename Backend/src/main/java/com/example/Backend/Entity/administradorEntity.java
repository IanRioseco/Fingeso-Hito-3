package com.example.Backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "administradores")
public class administradorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_admin")
    private Long id_admin;

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "Apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "Telefono", length = 50)
    private String telefono;

    /*Administrador->Reporte*/
    @OneToMany(mappedBy = "Admin", cascade = CascadeType.ALL)
    private List<reporteEntity> reportes = new ArrayList<>();


}

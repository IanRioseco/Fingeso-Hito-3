package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tiposdereporte")
public class tiporeporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_tipodereporte")
    private Long id_tipodereporte;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 256, nullable = false)
    private String descripcion;


}

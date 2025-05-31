package com.example.Backend.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "reportes")
public class reporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_reportes")
    private int Id_reporte;

    @Column(name = "fecha", nullable = false)
    private LocalDate Fecha;

    @Column(name = "contenido", length = 256)
    private String Contenido;

    @ManyToOne
    @JoinColumn(name = "Id_admin", referencedColumnName = "Id_admin")
    private administradorEntity admin;

    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    @OneToOne
    @JoinColumn(name = "Id_tiporeporte", referencedColumnName = "Id_tiporeporte")
    private tiporeporteEntity tiporeporte;



}

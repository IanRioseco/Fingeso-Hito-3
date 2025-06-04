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
    private Long Id_reporte;

    @Column(name = "fecha", nullable = false)
    private LocalDate Fecha;

    @Column(name = "contenido", length = 256)
    private String Contenido;

    /*Reporte->Medico*/
    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    /*Reporte->TipoReporte*/
    @OneToOne
    @JoinColumn(name = "Id_tipodereporte", referencedColumnName = "Id_tipodereporte")
    private tiporeporteEntity tipodereporte;

    /*Reporte->Administrador*/
    @ManyToOne
    @JoinColumn(name = "Id_admin", referencedColumnName = "Id_admin")
    private administradorEntity Admin;


}
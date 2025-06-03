package com.example.Backend.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "medicamentos")
public class medicamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_medicamento")
    private Long idmedicamento;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_vencimieto", length = 50, nullable = false)
    private String fecha_vencimieto;

    @Column(name = "descripcion", length = 256, nullable = false)
    private String descripcion;

    /*Medicamento->Farmacia-Medicamento*/
    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL)
    private List<farmacia_medicamentoEntity> farmaciaMedicamentos = new ArrayList<>();

    /*Medicamento->Receta-Medicamento*/
    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL)
    private List<receta_medicamentoEntity> recetaMedicamentos = new ArrayList<>();

}

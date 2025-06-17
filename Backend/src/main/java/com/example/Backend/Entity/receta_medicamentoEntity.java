package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "receta_medicamentoEntity")
public class receta_medicamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_recetamedicamento")
    private Long id_recetamedicamento;

    /*Receta-Medicamento->Receta*/
    @ManyToOne
    @JoinColumn(name = "Id_receta", referencedColumnName = "Id_receta")
    private recetaEntity receta;

    /*Receta-Medicamento->Medicamento*/
    @ManyToOne
    @JoinColumn(name = "Id_medicamento", referencedColumnName = "Id_medicamento")
    private medicamentoEntity medicamento;



}

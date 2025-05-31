package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "receta_medicamentoEntity")
public class receta_medicamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_recetamedicamento")
    private Long id_recetamedicamento;

    @ManyToOne
    @JoinColumn(name = "Id_medicamento", referencedColumnName = "Id_medicamento")
    private medicamentoEntity medicamento;

    @ManyToOne
    @JoinColumn(name = "Id_receta", referencedColumnName = "Id_receta")
    private recetaEntity receta;





}

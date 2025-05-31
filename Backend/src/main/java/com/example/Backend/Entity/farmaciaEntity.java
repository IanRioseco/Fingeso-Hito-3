package com.example.Backend.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "farmacias")
public class farmaciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_farmacia")
    private Long idFarmacia;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "farmacias", cascade = CascadeType.ALL)
    private List<farmaceuticoEntity> farmaceuticos = new ArrayList<>();

    @OneToMany(mappedBy = "farmacias", cascade = CascadeType.ALL)
    private List<farmacia_medicamentoEntity> farmaciaMedicamento = new ArrayList<>();

}

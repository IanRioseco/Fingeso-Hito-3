package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recetas")
public class recetaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_receta")
    private Long id_receta;

    @Column(name = "fecha", length = 10, nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    @ManyToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;

    @ManyToOne
    @JoinColumn(name = "Id_citamedica",referencedColumnName = "Id_citamedica")
    private citamedicaEntity citamedica;

    @OneToMany(mappedBy = "recetas", cascade = CascadeType.ALL)
    private List<receta_medicamentoEntity> recetaMedicamento = new ArrayList<>();

}

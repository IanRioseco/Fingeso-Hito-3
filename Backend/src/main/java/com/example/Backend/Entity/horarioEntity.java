package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Horariosmedicos")
public class horarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_horario")
    private Long idHorario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "horainicio", nullable = false)
    private LocalTime horainicio;

    @Column(name = "horafin", nullable = false)
    private LocalTime horafin;

    /*Horario->Medico*/
    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    /*Horario->Citamedica*/
    @ManyToOne
    @JoinColumn(name = "Id_citamedica", referencedColumnName = "Id_citamedica")
    private citamedicaEntity citamedica;
}

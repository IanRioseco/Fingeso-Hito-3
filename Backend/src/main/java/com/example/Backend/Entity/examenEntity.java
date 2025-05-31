package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Entity
@Table(name = "examenes")
public class examenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_examen")
    private Long IdExamen;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    @ManyToOne
    @JoinColumn(name = "Id_fichamedica", referencedColumnName = "Id_fichamedica")
    private fichamedicaEntity fichamedica;

    @ManyToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;
}

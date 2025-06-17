package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;



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

    /*Examen->Medico*/
    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    /*Solamente se deja en uno*/
    /*Examen->FichaMedica*/
    @ManyToOne
    @JoinColumn(name = "Id_fichamedica", referencedColumnName = "Id_fichamedica")
    private fichamedicaEntity fichamedica;

    /*Examen->Paciente*/
    @ManyToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;
}

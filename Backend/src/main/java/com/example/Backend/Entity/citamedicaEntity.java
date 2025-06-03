package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "citas_medicas")
public class citamedicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_citamedica")
    private Long Id_citamedica;

    @Column(name = "Estado", nullable = false)
    private String Estado;

    /*Citamedica->Recepcionista*/
    @ManyToOne
    @JoinColumn(name = "Id_recepcionista", referencedColumnName = "Id_recepcionista")
    private recepcionistaEntity recepcionista;

    /*Citamedica->Medico*/
    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    /*Citamedica->Paciente*/
    @ManyToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;


}

package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "citas_medicas")
public class citamedicaEntity {

    @Id
    @Column(name = "Id_citamedica")
    private Long Id_citamedica;

    @Column(name = "Estado")
    private String Estado;

    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    @ManyToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;

    @ManyToOne
    @JoinColumn(name = "Id_fichamedica", referencedColumnName = "Id_fichamedica")
    private fichamedicaEntity fichamedica;

}

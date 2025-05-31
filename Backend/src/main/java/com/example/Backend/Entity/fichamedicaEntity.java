package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fichasmedicas")
public class fichamedicaEntity {

    @Id
    @Column(name = "Id_fichamedica")
    private Long Id_fichamedica;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "sexo", length = 50)
    private String sexo;

    @Column(name = "sintomas", length = 1000)
    private String sintomas;

    @Column(name = "enfermedades", length = 1000)
    private String enfermedades;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "altura")
    private Double altura;

    @OneToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;

}

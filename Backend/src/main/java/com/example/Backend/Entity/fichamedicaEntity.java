package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "fichasmedicas")
public class fichamedicaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /*FichaMedica -> Examen*/
    @OneToMany(mappedBy = "fichamedica", cascade = CascadeType.ALL)
    private List<examenEntity> examenes =  new ArrayList<>();

    /*FichaMedica -> PersonalTecnico*/
    @OneToOne
    @JoinColumn(name = "Id_pertec", referencedColumnName = "Id_pertec")
    private personaltecnicoEntity personaltecnico;


}

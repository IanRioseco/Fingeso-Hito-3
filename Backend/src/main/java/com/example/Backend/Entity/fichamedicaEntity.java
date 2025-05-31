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

<<<<<<< Updated upstream
    @Column(name = "peso")
    private Double peso;

    @Column(name = "altura")
    private Double altura;

    @OneToOne
    @JoinColumn(name = "Id_paciente", referencedColumnName = "Id_paciente")
    private pacienteEntity paciente;
=======
    @OneToMany(mappedBy = "fichasmedicas", cascade = CascadeType.ALL)
    private List<examenEntity> examenes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "Id_pertec", referencedColumnName = "Id_pertec")
    private personaltecnicoEntity pertec;

>>>>>>> Stashed changes

}

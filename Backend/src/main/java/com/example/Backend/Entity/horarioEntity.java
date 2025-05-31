package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Horariosmedicos")
public class horarioEntity {

    @Id
    @Column(name = "Id_horario")
    private Long idHorario;

    @Column(name = "dia", length = 2)
    private String dia;

    @Column(name = "horainicio", length = 10)
    private String horainicio;

    @Column(name = "horafin", length = 10)
    private String horafin;

    @ManyToOne
    @JoinColumn(name = "Id_medico", referencedColumnName = "Id_medico")
    private medicoEntity medico;

    @OneToOne //podria ser muchos a muchos
    @JoinColumn(name = "Id_citamedica", referencedColumnName = "Id_citamedica")
    private citamedicaEntity citamedica;
}

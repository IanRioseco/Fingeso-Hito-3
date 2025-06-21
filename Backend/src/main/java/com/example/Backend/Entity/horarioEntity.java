package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "Horariosmedicos")
public class horarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_horario")
    private Long idHorario;

    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

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

    // Método para convertir la fecha a LocalDate si es necesario
    public LocalDate getFechaAsLocalDate() {
        if (fecha != null) {
            return fecha.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }

    // Método para establecer la fecha desde LocalDate
    public void setFechaFromLocalDate(LocalDate localDate) {
        if (localDate != null) {
            this.fecha = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }
}

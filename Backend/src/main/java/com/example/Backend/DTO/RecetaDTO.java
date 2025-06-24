package com.example.Backend.DTO;

import java.time.LocalDate;
import java.util.List;

public class RecetaDTO {
    private LocalDate fecha;
    private Long id_medico;
    private Long id_paciente;
    private Long id_citamedica;
    private List<Long> medicamentos; // IDs de medicamentos seleccionados
    private String descripcion;

    public LocalDate getFecha() {
        return fecha;
    }

    public Iterable<? extends Long> getMedicamentos() {
        return medicamentos;
    }

    public Long getId_medico() {
        return id_medico;
    }

    public Long getId_paciente() {
        return id_paciente;
    }
    public Long getId_citamedica() {
        return id_citamedica;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
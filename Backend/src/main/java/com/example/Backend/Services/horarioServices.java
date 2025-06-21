package com.example.Backend.Services;

import com.example.Backend.DTO.HorarioDTO;
import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Repository.horarioRepository;
import com.example.Backend.Repository.medicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class horarioServices {
    
    private  horarioRepository horarioRepo;
    private  medicoRepository medicoRepo;
    
    @Autowired
    public horarioServices(horarioRepository horarioRepo, medicoRepository medicoRepo) {
        this.horarioRepo = horarioRepo;
        this.medicoRepo = medicoRepo;
    }

    // Método para registrar un solo bloque validando traslapes
    public horarioEntity registrarHorarioValidado(horarioEntity horario) {
        if (horario.getFecha() == null || horario.getHorainicio() == null || horario.getHorafin() == null) {
            throw new IllegalArgumentException("Debe ingresar todos los campos requeridos.");
        }
        if (horario.getHorainicio().isAfter(horario.getHorafin()) || horario.getHorainicio().equals(horario.getHorafin())) {
            throw new IllegalArgumentException("Hora de inicio debe ser menor que la hora de fin.");
        }
        List<horarioEntity> existentes = horarioRepo.findByMedico_Idmedico(horario.getMedico().getIdmedico());
        boolean traslape = existentes.stream().anyMatch(h ->
                h.getFecha().equals(horario.getFecha()) &&
                        (
                                (horario.getHorainicio().isBefore(h.getHorafin()) && horario.getHorainicio().isAfter(h.getHorainicio())) ||
                                        (horario.getHorafin().isAfter(h.getHorainicio()) && horario.getHorafin().isBefore(h.getHorafin())) ||
                                        (horario.getHorainicio().equals(h.getHorainicio())) ||
                                        (horario.getHorafin().equals(h.getHorafin()))
                        )
        );
        if (traslape) {
            throw new IllegalStateException("El horario ingresado se traslapa con uno existente.");
        }
        return horarioRepo.save(horario);
    }

    public List<horarioEntity> obtenerTodosHorarios() {
        return horarioRepo.findAll();
    }
    
    public Optional<horarioEntity> obtenerHorarioPorId(Long id) {
        return horarioRepo.findById(id);
    }
    
    public horarioEntity actualizarHorario(horarioEntity horario) {
        return horarioRepo.save(horario);
    }
    
    public void eliminarHorario(Long id) {
        horarioRepo.deleteById(id);
    }

    public List<horarioEntity> obtenerHorariosPorMedico(Long idMedico) {
        return horarioRepo.findByMedico_Idmedico(idMedico);
    }

    // NUEVO: Método para registrar múltiples bloques desde DTOs
    public void registrarMultiplesHorarios(List<HorarioDTO> bloques) {
        for (HorarioDTO dto : bloques) {
            horarioEntity horario = new horarioEntity();
            horario.setFecha(LocalDate.parse(dto.getFecha()));
            horario.setHorainicio(LocalTime.parse(dto.getHorainicio()));
            horario.setHorafin(LocalTime.parse(dto.getHorafin()));
            medicoEntity medico = medicoRepo.findById(dto.getIdMedico())
                    .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));
            horario.setMedico(medico);
            // Usa la validación de traslapes
            registrarHorarioValidado(horario);
        }
    }
}
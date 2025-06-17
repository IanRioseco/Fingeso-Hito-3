package com.example.Backend.Services;

import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Repository.horarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class horarioServices {
    
    private  horarioRepository horarioRepo;
    
    @Autowired
    public horarioServices(horarioRepository horarioRepo) {
        this.horarioRepo = horarioRepo;
    }

    public horarioEntity registrarHorarioValidado(horarioEntity horario) {
        // Validación: campos obligatorios
        if (horario.getFecha() == null || horario.getHorainicio() == null || horario.getHorafin() == null) {
            throw new IllegalArgumentException("Debe ingresar todos los campos requeridos.");
        }

        if (horario.getHorainicio().isAfter(horario.getHorafin()) || horario.getHorainicio().equals(horario.getHorafin())) {
            throw new IllegalArgumentException("Hora de inicio debe ser menor que la hora de fin.");
        }

        // Validación: traslapes
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
}
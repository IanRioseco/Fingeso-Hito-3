package com.example.Backend.Services;

import com.example.Backend.Entity.citamedicaEntity;
import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Repository.citamedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Backend.Repository.horarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class citamedicaServices {
    private final citamedicaRepository citamedicaRepo;
    private final horarioRepository horarioRepo;

    @Autowired
    public citamedicaServices(citamedicaRepository citamedicaRepo, horarioRepository horarioRepo) {
        this.citamedicaRepo = citamedicaRepo;
        this.horarioRepo = horarioRepo;
    }

    public citamedicaEntity guardarCitamedica(citamedicaEntity citamedica) {
        return citamedicaRepo.save(citamedica);
    }

    public List<citamedicaEntity> ObtenerLasCitamedica() {
        return citamedicaRepo.findAll();
    }

    public Optional<citamedicaEntity> obtenerCitamedicaPorId(Long id) {
        return citamedicaRepo.findById(id);
    }

    public citamedicaEntity actualizarCitamedica(citamedicaEntity citamedica) {
        return citamedicaRepo.save(citamedica);
    }

    public void eliminarCitamedica(Long id) {
        citamedicaRepo.deleteById(id);
    }

    public List<citamedicaEntity> obtenerCitasPorPaciente(Long idPaciente) {
        List<citamedicaEntity> citas = citamedicaRepo.findByPacienteId(idPaciente);
        // Para cada cita, buscar y asociar el horario correspondiente (si existe)
        for (citamedicaEntity cita : citas) {
            List<horarioEntity> horarios = horarioRepo.findByCitamedicaId(cita.getId_citamedica());
            if (!horarios.isEmpty()) {
                horarioEntity horario = horarios.get(0);
                horario.setCitamedica(null); // Rompe la referencia cíclica para evitar bucles infinitos
                cita.setHorario(horario);
            }
        }
        return citas;
    }
}
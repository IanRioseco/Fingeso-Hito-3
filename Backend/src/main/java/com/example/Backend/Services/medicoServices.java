package com.example.Backend.Services;

import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Repository.medicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class medicoServices {

    private medicoRepository medicoRepo;

    @Autowired
    public medicoServices(medicoRepository medicoRepo) {
        this.medicoRepo = medicoRepo;
    }

    public medicoEntity guardarMedico(medicoEntity medico) {
        return medicoRepo.save(medico);
    }

    public List<medicoEntity> obtenerTodosMedicos() {
        return medicoRepo.findAll();
    }

    public Optional<medicoEntity> obtenerMedicoPorId(Long id) {
        return medicoRepo.findById(id);
    }

    public medicoEntity actualizarMedico(medicoEntity medico) {
        return medicoRepo.save(medico);
    }

    public void eliminarMedico(Long id) {
        medicoRepo.deleteById(id);
    }

    public List<medicoEntity> obtenerMedicosPorEspecialidad(Long Id_especialidad) {
        return medicoRepo.findByEspecialidadId(Id_especialidad);
    }


}

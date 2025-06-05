package com.example.Backend.Services;

import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Repository.medicoRepository;
import com.example.Backend.Repository.pacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class pacienteServices {

    private pacienteRepository pacienteRepo;

    @Autowired
    public pacienteServices(pacienteRepository pacienteRepo) {
        this.pacienteRepo = pacienteRepo;
    }

    public pacienteEntity guardarpaciente(pacienteEntity paciente) {
        return pacienteRepo.save(paciente);
    }

    public List<pacienteEntity> obtenerTodosPacientes () {
        return pacienteRepo.findAll();
    }

    public Optional<pacienteEntity> obtenerPacientePorId(Long id) {
        return pacienteRepo.findById(id);
    }

    public pacienteEntity actualizarPaciente(pacienteEntity paciente) {
        return pacienteRepo.save(paciente);
    }

    public void eliminarPaciente(Long id) {
        pacienteRepo.deleteById(id);
    }
}

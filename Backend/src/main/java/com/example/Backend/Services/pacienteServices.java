package com.example.Backend.Services;

import com.example.Backend.Entity.fichamedicaEntity;
import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Repository.fichamedicaRepository;
import com.example.Backend.Repository.medicoRepository;
import com.example.Backend.Repository.pacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class pacienteServices {

    private pacienteRepository pacienteRepo;
    private fichamedicaRepository fichamedicaRepository;

    @Autowired
    public pacienteServices(pacienteRepository pacienteRepo, fichamedicaRepository fichamedicaRepository) {
        this.pacienteRepo = pacienteRepo;
        this.fichamedicaRepository = fichamedicaRepository;
    }

    public pacienteEntity registrarPaciente(pacienteEntity paciente) {
        fichamedicaEntity ficha = paciente.getFichamedica();
        if (ficha != null) {
            ficha.setPaciente(paciente); // Relación bidireccional
        }
        // Guardar primero el paciente para que tenga Id
        pacienteEntity pacienteGuardado = pacienteRepo.save(paciente);
        if (ficha != null) {
            ficha.setPaciente(pacienteGuardado);
            fichamedicaRepository.save(ficha);
        }
        return pacienteGuardado;
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

package com.example.Backend.Services;

import com.example.Backend.Entity.examenEntity;
import com.example.Backend.Repository.examenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class examenServices {
    private final examenRepository examenRepo;

    @Autowired
    public examenServices(examenRepository examenRepo) {
        this.examenRepo = examenRepo;
    }

    public examenEntity guardarExamen(examenEntity examen) {
        return examenRepo.save(examen);
    }

    public List<examenEntity> obtenerTodosExamenes() {
        return examenRepo.findAll();
    }

    public Optional<examenEntity> obtenerExamenPorId(Long id) {
        return examenRepo.findById(id);
    }

    public examenEntity actualizarExamen(examenEntity examen) {
        return examenRepo.save(examen);
    }

    public void eliminarExamen(Long id) {
        examenRepo.deleteById(id);
    }
}
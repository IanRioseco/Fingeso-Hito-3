package com.example.Backend.Services;

import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Repository.recetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class recetaServices {
    private recetaRepository recetaRepo;

    @Autowired
    public recetaServices(recetaRepository recetaRepo) {
        this.recetaRepo = recetaRepo;
    }

    public recetaEntity guardarReceta(recetaEntity receta) {
        return recetaRepo.save(receta);
    }

    public List<recetaEntity> obtenerTodasLasRecetas() {
        return recetaRepo.findAll();
    }

    public Optional<recetaEntity> obtenerRecetaPorId(Long id) {
        return recetaRepo.findById(id);
    }

    public recetaEntity actualizarReceta(recetaEntity receta) {
        return recetaRepo.save(receta);
    }

    public void eliminarReceta(Long id) {
        recetaRepo.deleteById(id);
    }
}

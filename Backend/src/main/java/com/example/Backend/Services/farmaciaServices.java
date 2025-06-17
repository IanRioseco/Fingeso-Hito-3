package com.example.Backend.Services;

import com.example.Backend.Entity.farmaciaEntity;
import com.example.Backend.Repository.farmaciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class farmaciaServices {
    private farmaciaRepository farmaciaRepo;

    @Autowired
    public farmaciaServices(farmaciaRepository farmaciaRepo) {
        this.farmaciaRepo = farmaciaRepo;
    }

    public farmaciaEntity guardarFarmacia(farmaciaEntity farmacia) {
        return farmaciaRepo.save(farmacia);
    }

    public List<farmaciaEntity> obtenerTodasFarmacias() {
        return farmaciaRepo.findAll();
    }

    public Optional<farmaciaEntity> obtenerFarmaciaPorId(Long id) {
        return farmaciaRepo.findById(id);
    }

    public farmaciaEntity actualizarFarmacia(farmaciaEntity farmacia) {
        return farmaciaRepo.save(farmacia);
    }

    public void eliminarFarmacia(Long id) {
        farmaciaRepo.deleteById(id);
    }

    public void eliminarTodasLasFarmacias() {
        farmaciaRepo.deleteAll();
    }
}
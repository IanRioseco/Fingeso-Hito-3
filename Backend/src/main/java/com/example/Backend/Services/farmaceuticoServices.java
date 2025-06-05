package com.example.Backend.Services;

import com.example.Backend.Entity.farmaceuticoEntity;
import com.example.Backend.Repository.farmaceuticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class farmaceuticoServices {
    private farmaceuticoRepository farmaceuticoRepo;

    @Autowired
    public farmaceuticoServices(farmaceuticoRepository farmaceuticoRepo) {
        this.farmaceuticoRepo = farmaceuticoRepo;
    }

    public farmaceuticoEntity guardarFarmaceutico(farmaceuticoEntity farmaceutico) {
        return farmaceuticoRepo.save(farmaceutico);
    }

    public List<farmaceuticoEntity> obtenerTodosFarmaceuticos() {
        return farmaceuticoRepo.findAll();
    }

    public Optional<farmaceuticoEntity> obtenerFarmaceuticoPorId(Long id) {
        return farmaceuticoRepo.findById(id);
    }

    public farmaceuticoEntity actualizarFarmaceutico(farmaceuticoEntity farmaceutico) {
        return farmaceuticoRepo.save(farmaceutico);
    }

    public void eliminarFarmaceutico(Long id) {
        farmaceuticoRepo.deleteById(id);
    }
}

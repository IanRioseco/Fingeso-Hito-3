package com.example.Backend.Services;

import com.example.Backend.Entity.farmaceuticoEntity;
import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Entity.farmaciaEntity;
import com.example.Backend.Repository.farmaceuticoRepository;
import com.example.Backend.Repository.rolRepository;
import com.example.Backend.Repository.farmaciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class farmaceuticoServices {

    private final farmaceuticoRepository farmaceuticoRepo;
    private final rolRepository rolRepo;
    private final farmaciaRepository farmaciaRepo;

    @Autowired
    public farmaceuticoServices(farmaceuticoRepository farmaceuticoRepo, rolRepository rolRepo, farmaciaRepository farmaciaRepo) {
        this.farmaceuticoRepo = farmaceuticoRepo;
        this.rolRepo = rolRepo;
        this.farmaciaRepo = farmaciaRepo;
    }

    public farmaceuticoEntity guardarFarmaceutico(farmaceuticoEntity farmaceutico) {
        // Verificar si el rol existe
        rolEntity rol = rolRepo.findById(farmaceutico.getRol().getId_rol()) // Uso getId_rol
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + farmaceutico.getRol().getId_rol()));
        farmaceutico.setRol(rol);

        // Asociar farmacia si viene el id
        if (farmaceutico.getFarmacia() != null && farmaceutico.getFarmacia().getIdFarmacia() != null) {
            farmaciaEntity farmacia = farmaciaRepo.findById(farmaceutico.getFarmacia().getIdFarmacia())
                    .orElseThrow(() -> new RuntimeException("Farmacia no encontrada con ID: " + farmaceutico.getFarmacia().getIdFarmacia()));
            farmaceutico.setFarmacia(farmacia);
        } else {
            farmaceutico.setFarmacia(null);
        }

        return farmaceuticoRepo.save(farmaceutico);
    }

    public List<farmaceuticoEntity> obtenerTodosFarmaceuticos() {
        return farmaceuticoRepo.findAll();
    }

    public Optional<farmaceuticoEntity> obtenerFarmaceuticoPorId(Long id) {
        return farmaceuticoRepo.findById(id);
    }

    public farmaceuticoEntity actualizarFarmaceutico(farmaceuticoEntity farmaceutico) {
        // Asociar farmacia si viene el id
        if (farmaceutico.getFarmacia() != null && farmaceutico.getFarmacia().getIdFarmacia() != null) {
            farmaciaEntity farmacia = farmaciaRepo.findById(farmaceutico.getFarmacia().getIdFarmacia())
                    .orElseThrow(() -> new RuntimeException("Farmacia no encontrada con ID: " + farmaceutico.getFarmacia().getIdFarmacia()));
            farmaceutico.setFarmacia(farmacia);
        } else {
            farmaceutico.setFarmacia(null);
        }
        return farmaceuticoRepo.save(farmaceutico);
    }

    public void eliminarFarmaceutico(Long id) {
        farmaceuticoRepo.deleteById(id);
    }
}
package com.example.Backend.Services;

import com.example.Backend.Entity.farmaceuticoEntity;
import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Repository.farmaceuticoRepository;
import com.example.Backend.Repository.rolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class farmaceuticoServices {

    private final farmaceuticoRepository farmaceuticoRepo;
    private final rolRepository rolRepo;

    @Autowired
    public farmaceuticoServices(farmaceuticoRepository farmaceuticoRepo, rolRepository rolRepo) {
        this.farmaceuticoRepo = farmaceuticoRepo;
        this.rolRepo = rolRepo;
    }

    public farmaceuticoEntity guardarFarmaceutico(farmaceuticoEntity farmaceutico) {
        // Verificar si el rol existe
        rolEntity rol = rolRepo.findById(farmaceutico.getRol().getId_rol()) // Uso getId_rol
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + farmaceutico.getRol().getId_rol()));

        // Asignar el rol al farmaceutico
        farmaceutico.setRol(rol);

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
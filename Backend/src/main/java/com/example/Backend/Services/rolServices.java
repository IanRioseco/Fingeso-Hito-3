package com.example.Backend.Services;

import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Repository.rolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class rolServices {
    private rolRepository rolRepo;

    @Autowired
    public rolServices(rolRepository rolRepo) {
        this.rolRepo = rolRepo;
    }

    public rolEntity GuardarRol(rolEntity rol) {
        return rolRepo.save(rol);
    }

    public List<rolEntity> obtenerTodosRoles() {
        return rolRepo.findAll();
    }

    public Optional<rolEntity> obtenerRolPorId(Long id) {
        return rolRepo.findById(id);
    }

    public rolEntity actualizarRol(rolEntity rol) {
        return rolRepo.save(rol);
    }

    public void eliminarRol(Long id) {
        rolRepo.deleteById(id);
    }

    public boolean existeRolPorNombre(String nombre) {
        return rolRepo.findByNombre(nombre).isPresent();
    }

    public void eliminarTodosLosRoles() {
        rolRepo.deleteAll();
    }
}

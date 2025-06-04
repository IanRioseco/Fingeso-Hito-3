package com.example.Backend.Services;

import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Repository.rolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public List<rolEntity> ObtenerTodoLosRoles(){
        return rolRepo.findAll();
    }

    public Optional<rolEntity> ObtenerRolId(Long Id) {
        return rolRepo.findById(Id);
    }

    public void eliminarRol(Long Id) {
        rolRepo.deleteById(Id);
    }

    public rolEntity ActualizarRol(rolEntity rol) {
        return rolRepo.save(rol);
    }

}

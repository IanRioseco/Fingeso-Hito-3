package com.example.Backend.Services;

import com.example.Backend.Entity.citamedicaEntity;
import com.example.Backend.Repository.citamedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class citamedicaServices {
    private citamedicaRepository citamedicaRepo;

    @Autowired
    public citamedicaServices(citamedicaRepository citamedicaRepo) {
        this.citamedicaRepo = citamedicaRepo;
    }

    public citamedicaEntity guardarCitamedica(citamedicaEntity citamedica) {
        return citamedicaRepo.save(citamedica);
    }

    public List<citamedicaEntity> ObtenerLasCitamedica() {
        return citamedicaRepo.findAll();
    }

    public Optional<citamedicaEntity> obtenerCitamedicaPorId(Long id) {
        return citamedicaRepo.findById(id);
    }

    public citamedicaEntity actualizarCitamedica(citamedicaEntity citamedica) {
        return citamedicaRepo.save(citamedica);
    }

    public void eliminarCitamedica(Long id) {
        citamedicaRepo.deleteById(id);
    }
}
package com.example.Backend.Services;

import com.example.Backend.Entity.fichamedicaEntity;
import com.example.Backend.Repository.fichamedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class fichamedicaServices {

    private fichamedicaRepository fichamedicaRepo;

    @Autowired
    public fichamedicaServices(fichamedicaRepository fichamedicaRepo) {
        this.fichamedicaRepo = fichamedicaRepo;
    }

    public fichamedicaEntity guardarFichaMedica(fichamedicaEntity fichamedica) {
        return fichamedicaRepo.save(fichamedica);
    }

    public List<fichamedicaEntity> obtenerTodasLasFichasMedicas() {
        return fichamedicaRepo.findAll();
    }

    public Optional<fichamedicaEntity> obtenerFichaMedicaPorId(Long id) {
        return fichamedicaRepo.findById(id);
    }

    public fichamedicaEntity actualizarFichaMedica(fichamedicaEntity fichamedica) {
        return fichamedicaRepo.save(fichamedica);
    }

    public void eliminarFichaMedica(Long id) {
        fichamedicaRepo.deleteById(id);
    }
}

package com.example.Backend.Services;

import com.example.Backend.Entity.especialidadEntity;
import com.example.Backend.Repository.especialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class especialidadServices {
    private especialidadRepository especialidadRepo;

    @Autowired
    public especialidadServices(especialidadRepository especialidadRepo) {
        this.especialidadRepo = especialidadRepo;
    }

    public especialidadEntity guardarespecialidad (especialidadEntity especialidad) {
        return especialidadRepo.save(especialidad);
    }

    public List<especialidadEntity> Obtenerespecialidades () {
        return especialidadRepo.findAll();
    }

    public Optional<especialidadEntity> ObtenerespecialidadPorId (Long id) {
        return especialidadRepo.findById(id);
    }

    public void eliminarespecialidadPorId (Long id) {
        especialidadRepo.deleteById(id);
    }

    public especialidadEntity actualizarespecialidad (especialidadEntity especialidad) {
        return especialidadRepo.save(especialidad);
    }


}

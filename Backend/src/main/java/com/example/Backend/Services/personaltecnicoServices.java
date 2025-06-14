package com.example.Backend.Services;

import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Entity.personaltecnicoEntity;
import com.example.Backend.Repository.pacienteRepository;
import com.example.Backend.Repository.personaltecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class personaltecnicoServices {

    private personaltecnicoRepository personaltecnicoRepo;

    @Autowired
    public personaltecnicoServices(personaltecnicoRepository personaltecnicoRepo) {
        this.personaltecnicoRepo = personaltecnicoRepo;
    }

    public personaltecnicoEntity guardarPersonalTecnico(personaltecnicoEntity personaltecnico) {
        return personaltecnicoRepo.save(personaltecnico);
    }

    public List<personaltecnicoEntity> obtenerTodosPersonalTecnicos() {
        return personaltecnicoRepo.findAll();
    }

    public Optional<personaltecnicoEntity> obtenerPersonalTecnicoPorId(Long id) {
        return personaltecnicoRepo.findById(id);
    }

    public personaltecnicoEntity actualizarPersonalTecnico(personaltecnicoEntity personaltecnico) {
        return personaltecnicoRepo.save(personaltecnico);
    }

    public void eliminarPersonalTecnico(Long id) {
        personaltecnicoRepo.deleteById(id);
    }
}

package com.example.Backend.Services;

import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Repository.horarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class horarioServices {
    
    private  horarioRepository horarioRepo;
    
    @Autowired
    public horarioServices(horarioRepository horarioRepo) {
        this.horarioRepo = horarioRepo;
    }
    
    public horarioEntity guardarHorario(horarioEntity horario) {
        return horarioRepo.save(horario);
    }
    
    public List<horarioEntity> obtenerTodosHorarios() {
        return horarioRepo.findAll();
    }
    
    public Optional<horarioEntity> obtenerHorarioPorId(Long id) {
        return horarioRepo.findById(id);
    }
    
    public horarioEntity actualizarHorario(horarioEntity horario) {
        return horarioRepo.save(horario);
    }
    
    public void eliminarHorario(Long id) {
        horarioRepo.deleteById(id);
    }
}
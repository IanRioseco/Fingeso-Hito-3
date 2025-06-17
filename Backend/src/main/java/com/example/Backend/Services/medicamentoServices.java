package com.example.Backend.Services;

import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Repository.medicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class medicamentoServices {
    
    private medicamentoRepository medicamentoRepo;
    
    @Autowired
    public medicamentoServices(medicamentoRepository medicamentoRepo) {
        this.medicamentoRepo = medicamentoRepo;
    }
    
    public medicamentoEntity guardarMedicamento(medicamentoEntity medicamento) {
        return medicamentoRepo.save(medicamento);
    }
    
    public List<medicamentoEntity> obtenerTodosMedicamentos() {
        return medicamentoRepo.findAll();
    }
    
    public Optional<medicamentoEntity> obtenerMedicamentoPorId(Long id) {
        return medicamentoRepo.findById(id);
    }
    
    public medicamentoEntity actualizarMedicamento(medicamentoEntity medicamento) {
        return medicamentoRepo.save(medicamento);
    }
    
    public void eliminarMedicamento(Long id) {
        medicamentoRepo.deleteById(id);
    }
}
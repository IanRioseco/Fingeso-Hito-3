package com.example.Backend.Services;

import com.example.Backend.Entity.farmacia_medicamentoEntity;
import com.example.Backend.Repository.farmacia_medicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class farmacia_medicamentoServices {
    private farmacia_medicamentoRepository farmacia_medicamentoRepo;

    @Autowired
    public farmacia_medicamentoServices(farmacia_medicamentoRepository farmacia_medicamentoRepo) {
        this.farmacia_medicamentoRepo = farmacia_medicamentoRepo;
    }

    public farmacia_medicamentoEntity guardarFarmaciaMedicamento(farmacia_medicamentoEntity farmaciaMedicamento) {
        return farmacia_medicamentoRepo.save(farmaciaMedicamento);
    }

    public List<farmacia_medicamentoEntity> obtenerTodosFarmaciaMedicamentos() {
        return farmacia_medicamentoRepo.findAll();
    }

    public Optional<farmacia_medicamentoEntity> obtenerFarmaciaMedicamentoPorId(Long id) {
        return farmacia_medicamentoRepo.findById(id);
    }

    public farmacia_medicamentoEntity actualizarFarmaciaMedicamento(farmacia_medicamentoEntity farmaciaMedicamento) {
        return farmacia_medicamentoRepo.save(farmaciaMedicamento);
    }

    public void eliminarFarmaciaMedicamento(Long id) {
        farmacia_medicamentoRepo.deleteById(id);
    }
}
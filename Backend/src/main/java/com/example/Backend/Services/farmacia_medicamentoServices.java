package com.example.Backend.Services;

import com.example.Backend.Entity.farmacia_medicamentoEntity;
import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Repository.farmacia_medicamentoRepository;
import com.example.Backend.Repository.medicamentoRepository;
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
    @Autowired
    private medicamentoRepository medicamentoRepo;

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
        // Buscar la relación existente
        farmacia_medicamentoEntity existente = farmacia_medicamentoRepo.findById(farmaciaMedicamento.getId_farmacia_medicamento())
                .orElseThrow(() -> new RuntimeException("No existe la relación farmacia-medicamento"));

        // Actualizar la farmacia si es necesario
        existente.setFarmacia(farmaciaMedicamento.getFarmacia());

        // Actualizar los campos del medicamento asociado
        medicamentoEntity medExistente = existente.getMedicamento();
        medicamentoEntity medEdit = farmaciaMedicamento.getMedicamento();

        medExistente.setNombre(medEdit.getNombre());
        medExistente.setTipo(medEdit.getTipo());
        medExistente.setMarca(medEdit.getMarca());
        medExistente.setCantidad(medEdit.getCantidad());
        medExistente.setFecha_vencimieto(medEdit.getFecha_vencimieto());
        medExistente.setDescripcion(medEdit.getDescripcion());

        // Guardar el medicamento actualizado en el repo correcto
        medicamentoRepo.save(medExistente);

        // Guardar la relación actualizada
        return farmacia_medicamentoRepo.save(existente);
    }

    public void eliminarFarmaciaMedicamento(Long id) {
        farmacia_medicamentoRepo.deleteById(id);
    }

    public List<farmacia_medicamentoEntity> obtenerPorFarmaciaId(Long farmaciaId) {
        return farmacia_medicamentoRepo.findByFarmaciaId(farmaciaId);
    }
}
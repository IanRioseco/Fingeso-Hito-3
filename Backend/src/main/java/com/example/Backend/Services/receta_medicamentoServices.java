package com.example.Backend.Services;

import com.example.Backend.Entity.receta_medicamentoEntity;
import com.example.Backend.Repository.receta_medicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class receta_medicamentoServices {

    private final receta_medicamentoRepository recetaMedicamentoRepo;

    @Autowired
    public receta_medicamentoServices(receta_medicamentoRepository recetaMedicamentoRepo) {
        this.recetaMedicamentoRepo = recetaMedicamentoRepo;
    }

    public receta_medicamentoEntity guardarRecetaMedicamento(receta_medicamentoEntity receta) {
        return recetaMedicamentoRepo.save(receta);
    }

    public List<receta_medicamentoEntity> obtenerTodasRecetasMedicamento() {
        return recetaMedicamentoRepo.findAll();
    }

    public Optional<receta_medicamentoEntity> obtenerRecetaMedicamentoPorId(Long id) {
        return recetaMedicamentoRepo.findById(id);
    }

    public receta_medicamentoEntity actualizarRecetaMedicamento(receta_medicamentoEntity receta) {
        return recetaMedicamentoRepo.save(receta);
    }

    public void eliminarRecetaMedicamento(Long id) {
        recetaMedicamentoRepo.deleteById(id);
    }
}

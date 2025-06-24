package com.example.Backend.Services;

import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Entity.receta_medicamentoEntity;
import com.example.Backend.Repository.medicamentoRepository;
import com.example.Backend.Repository.recetaRepository;
import com.example.Backend.Repository.receta_medicamentoRepository;
import com.example.Backend.DTO.ValidarRecetaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class receta_medicamentoServices {

    private final receta_medicamentoRepository recetaMedicamentoRepo;
    private final medicamentoRepository medicamentoRepo;
    private final recetaRepository recetaRepo;

    @Autowired
    public receta_medicamentoServices(receta_medicamentoRepository recetaMedicamentoRepo, medicamentoRepository medicamentoRepo, recetaRepository recetaRepo) {
        this.recetaMedicamentoRepo = recetaMedicamentoRepo;
        this.medicamentoRepo = medicamentoRepo;
        this.recetaRepo = recetaRepo;
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

    public List<medicamentoEntity> obtenerMedicamentosPorRecetaId(Long idReceta) {
        return recetaMedicamentoRepo.findMedicamentosByRecetaId(idReceta);
    }

    public List<recetaEntity> obtenerRecetasPorFarmacia(Long farmaciaId) {
        return recetaMedicamentoRepo.findRecetasByFarmaciaId(farmaciaId);
    }

    @Transactional
    public void validarRecetaYDescontarStock(ValidarRecetaRequest request) {
        for (ValidarRecetaRequest.DetalleEntrega detalle : request.getDetalles()) {
            medicamentoEntity med = medicamentoRepo.findByIdmedicamento(detalle.getIdMedicamento());
            if (med != null && med.getCantidad() != null && med.getCantidad() >= detalle.getCantidadEntregar()) {
                med.setCantidad(med.getCantidad() - detalle.getCantidadEntregar());
                medicamentoRepo.save(med);
            } else {
                throw new RuntimeException("Stock insuficiente para el medicamento ID: " + detalle.getIdMedicamento());
            }
        }
        // Marcar receta como validada
        recetaEntity receta = recetaRepo.findById(request.getIdReceta())
            .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
        receta.setEstado("VALIDADA");
        recetaRepo.save(receta);
    }
}

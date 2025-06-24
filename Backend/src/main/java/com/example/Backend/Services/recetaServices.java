package com.example.Backend.Services;

import com.example.Backend.DTO.RecetaDTO;
import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Entity.receta_medicamentoEntity;
import com.example.Backend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class recetaServices {
    private recetaRepository recetaRepo;
    private receta_medicamentoRepository recetaMedicamentoRepo;
    private citamedicaRepository citamedicaRepo;
    private pacienteRepository pacienteRepo;
    private medicoRepository medicoRepo;
    private medicamentoRepository medicamentoRepo;

    @Autowired
    public recetaServices(recetaRepository recetaRepo, receta_medicamentoRepository recetaMedicamentoRepo, citamedicaRepository citamedicaRepo,
                          pacienteRepository pacienteRepo, medicoRepository medicoRepo, medicamentoRepository medicamentoRepo) {
        this.recetaRepo = recetaRepo;
        this.recetaMedicamentoRepo = recetaMedicamentoRepo;
        this.citamedicaRepo = citamedicaRepo;
        this.pacienteRepo = pacienteRepo;
        this.medicoRepo = medicoRepo;
        this.medicamentoRepo = medicamentoRepo;
    }

    public recetaEntity guardarReceta(recetaEntity receta) {
        return recetaRepo.save(receta);
    }

    public List<recetaEntity> obtenerTodasLasRecetas() {
        return recetaRepo.findAll();
    }

    public Optional<recetaEntity> obtenerRecetaPorId(Long id) {
        return recetaRepo.findById(id);
    }

    public recetaEntity actualizarReceta(recetaEntity receta) {
        return recetaRepo.save(receta);
    }

    public void eliminarReceta(Long id) {
        recetaRepo.deleteById(id);
    }

    public recetaEntity crearRecetaConMedicamentos(RecetaDTO dto) {
        // 1. Crear y guardar la receta
        recetaEntity receta = new recetaEntity();
        receta.setFecha(dto.getFecha());
        receta.setMedico(medicoRepo.findById(dto.getId_medico()).orElseThrow());
        receta.setPaciente(pacienteRepo.findById(dto.getId_paciente()).orElseThrow());
        receta.setCitamedica(citamedicaRepo.findById(dto.getId_citamedica()).orElseThrow());
        receta.setDescripcion(dto.getDescripcion());
        receta = recetaRepo.save(receta); // Aquí se genera el id_receta

        // 2. Crear y guardar cada relación receta-medicamento
        for (Long idMed : dto.getMedicamentos()) {
            receta_medicamentoEntity rm = new receta_medicamentoEntity();
            rm.setReceta(receta);
            rm.setMedicamento(medicamentoRepo.findByIdmedicamento(idMed));
            recetaMedicamentoRepo.save(rm);
        }
        return receta;
    }

    public List<recetaEntity> obtenerRecetaPorCitamedicaId(Long idCita) {
        return recetaRepo.findByCitaId(idCita);
    }

}

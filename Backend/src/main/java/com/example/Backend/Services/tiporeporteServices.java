package com.example.Backend.Services;

import com.example.Backend.Entity.tiporeporteEntity;
import com.example.Backend.Repository.tiporeporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class tiporeporteServices {
    private tiporeporteRepository tiporeporteRepo;

    @Autowired
    public tiporeporteServices(tiporeporteRepository tiporeporteRepo) {
        this.tiporeporteRepo = tiporeporteRepo;
    }

    public tiporeporteEntity guardarTiporeporte(tiporeporteEntity tiporeporte) {
        return tiporeporteRepo.save(tiporeporte);
    }

    public List<tiporeporteEntity> ObtenerTodosTiporeporte() {
        return tiporeporteRepo.findAll();
    }

    public Optional<tiporeporteEntity> obtenerTiporeportePorId(Long id) {
        return tiporeporteRepo.findById(id);
    }

    public void eliminarTiporeporte(Long Id) {
        tiporeporteRepo.deleteById(Id);
    }

    public tiporeporteEntity actualizarTiporeporte(tiporeporteEntity tiporeporte) {
        return tiporeporteRepo.save(tiporeporte);
    }
}

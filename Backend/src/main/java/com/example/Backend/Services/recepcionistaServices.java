package com.example.Backend.Services;

import com.example.Backend.Entity.recepcionistaEntity;
import com.example.Backend.Repository.recepcionistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class recepcionistaServices {

    private recepcionistaRepository recepcionistaRepo;

    @Autowired
    public recepcionistaServices(recepcionistaRepository recepcionistaRepo) {
        this.recepcionistaRepo = recepcionistaRepo;
    }

    public recepcionistaEntity guardarRecepcionista(recepcionistaEntity recepcionista) {
        return recepcionistaRepo.save(recepcionista);
    }

    public List<recepcionistaEntity> obtenerTodosRecepcionistas() {
        return recepcionistaRepo.findAll();
    }

    public Optional<recepcionistaEntity> obtenerRecepcionistaPorId(Long id) {
        return recepcionistaRepo.findById(id);
    }

    public recepcionistaEntity actualizarRecepcionista(recepcionistaEntity recepcionista) {
        return recepcionistaRepo.save(recepcionista);
    }

    public void eliminarRecepcionista(Long id) {
        recepcionistaRepo.deleteById(id);
    }
}

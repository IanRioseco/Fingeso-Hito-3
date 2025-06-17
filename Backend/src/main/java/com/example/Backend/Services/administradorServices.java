package com.example.Backend.Services;

import com.example.Backend.Entity.administradorEntity;
import com.example.Backend.Repository.administradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



/*BD->Repository->Servicio(Logica de negocio)-> Controller */
@Service
public class administradorServices {
    private administradorRepository administradorRepo;

    @Autowired
    public administradorServices(administradorRepository administradorRepo) {
        this.administradorRepo = administradorRepo;
    }

    public administradorEntity guardarAdministrador(administradorEntity administrador) {
        return administradorRepo.save(administrador);
    }

    public List<administradorEntity> ObtenerTodosAdministrador()
    {
        return administradorRepo.findAll();
    }

    public Optional<administradorEntity> obtenerAdministradorPorId(Long id) {
        return administradorRepo.findById(id);
    }

    public void eliminarAdministrador(Long Id) {
        administradorRepo.deleteById(Id);
    }

    public administradorEntity actualizarAdministrador(administradorEntity administrador) {
        return administradorRepo.save(administrador);
    }

    public Optional<administradorEntity> obtenerAdministradorPorCorreo(String correo) {
        return administradorRepo.findByCorreo(correo);
    }

}

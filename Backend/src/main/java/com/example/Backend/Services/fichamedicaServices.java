package com.example.Backend.Services;

import com.example.Backend.Entity.fichamedicaEntity;
import com.example.Backend.Repository.fichamedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class fichamedicaServices {

    private fichamedicaRepository fichamedicaRepo;

    @Autowired
    public fichamedicaServices(fichamedicaRepository fichamedicaRepo) {
        this.fichamedicaRepo = fichamedicaRepo;
    }

    public fichamedicaEntity guardarFichaMedica(fichamedicaEntity fichamedica) {
        try {
            System.out.println("Guardando ficha médica: " + fichamedica);
            return fichamedicaRepo.save(fichamedica);
        } catch (Exception e) {
            System.err.println("Error al guardar ficha médica: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<fichamedicaEntity> obtenerTodasLasFichasMedicas() {
        return fichamedicaRepo.findAll();
    }

    public Optional<fichamedicaEntity> obtenerFichaMedicaPorId(Long id) {
        return fichamedicaRepo.findById(id);
    }

    public fichamedicaEntity actualizarFichaMedica(fichamedicaEntity fichamedica) {
        return fichamedicaRepo.save(fichamedica);
    }

    public void eliminarFichaMedica(Long id) {
        fichamedicaRepo.deleteById(id);
    }

    public fichamedicaEntity obtenerFichaMedicaPorPacienteId(Long id) {
        try {
            System.out.println("Buscando ficha médica para paciente ID: " + id);
            fichamedicaEntity ficha = fichamedicaRepo.findByPaciente_RutPa(id);
            if (ficha != null) {
                System.out.println("Ficha médica encontrada con ID: " + ficha.getId_fichamedica());
            } else {
                System.out.println("No se encontró ficha médica para el paciente ID: " + id);
            }
            return ficha;
        } catch (Exception e) {
            System.err.println("Error al buscar ficha médica por ID de paciente: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}

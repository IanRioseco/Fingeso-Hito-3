package com.example.Backend.Services;

import com.example.Backend.Entity.especialidadEntity;
import com.example.Backend.Repository.especialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class especialidadServices {
    private especialidadRepository especialidadRepo;

    // Inyección del EntityManager para ejecutar queries nativos
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public especialidadServices(especialidadRepository especialidadRepo) {
        this.especialidadRepo = especialidadRepo;
    }

    public especialidadEntity guardarEspecialidad(especialidadEntity especialidad) {
        return especialidadRepo.save(especialidad);
    }

    public List<especialidadEntity> obtenerTodasLasEspecialidades() {
        return especialidadRepo.findAll();
    }

    public Optional<especialidadEntity> obtenerEspecialidadPorId(Long id) {
        return especialidadRepo.findById(id);
    }

    public especialidadEntity actualizarEspecialidad(especialidadEntity especialidad) {
        return especialidadRepo.save(especialidad);
    }

    public void eliminarEspecialidad(Long id) {
        especialidadRepo.deleteById(id);
    }

    public void eliminarTodasLasespecialidades(){
        especialidadRepo.deleteAll();
    }

    /**
     * Reinicia el contador de la secuencia de IDs de la tabla especialidades a 1 (PostgreSQL).
     * Esto es útil si quieres que, después de eliminar todas las especialidades,
     * el próximo registro insertado tenga id = 1.
     * ¡Solo debe usarse en desarrollo o pruebas!
     */
    @Transactional
    public void reiniciarAutoIncrement() {
        // Ejecuta un query nativo para reiniciar la secuencia en PostgreSQL
        entityManager.createNativeQuery("ALTER SEQUENCE especialidades_id_especialidad_seq RESTART WITH 1").executeUpdate();
    }
}
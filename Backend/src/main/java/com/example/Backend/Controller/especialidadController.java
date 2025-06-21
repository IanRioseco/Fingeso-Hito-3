package com.example.Backend.Controller;
import com.example.Backend.Entity.especialidadEntity;
import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Services.especialidadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/especialidad")
public class especialidadController {
    private especialidadServices especialidadServ;

    @Autowired
    public especialidadController(especialidadServices especialidadServ) {
        this.especialidadServ = especialidadServ;
    }

    @PostMapping("/")
    public ResponseEntity<especialidadEntity> crearEspecialidad(@RequestBody especialidadEntity especialidad) {
        especialidadEntity nuevaEspecialidad = especialidadServ.guardarEspecialidad(especialidad);
        return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
    }

    @PostMapping("/initialize")
    public ResponseEntity<List<especialidadEntity>> initializeEspecialidades() {
        // Primero limpiamos los roles existentes
        especialidadServ.eliminarTodasLasespecialidades();

        List<especialidadEntity> especialidadesCreadas = new ArrayList<>();

        String[] especialidades = {
                "Cardiología",     // ID 1
                "Dermatología",       // ID 2
                "Pediatría",      // ID 3
                "Neurología", // ID 4
                "Oftalmología", // ID 5
                "Ortopedia",
                "Ginecología"
                ,"Urología"// ID 6
        };
        for (String especialidadesNombre : especialidades) {
            especialidadEntity nuevaEsp = new especialidadEntity();
            nuevaEsp.setNombre(especialidadesNombre);
            especialidadesCreadas.add(especialidadServ.guardarEspecialidad(nuevaEsp));
        }
        return ResponseEntity.ok(especialidadesCreadas);
    }
    @GetMapping("/")
    public ResponseEntity<List<especialidadEntity>> obtenerTodasEspecialidades() {
        List<especialidadEntity> especialidades = especialidadServ.obtenerTodasLasEspecialidades();
        return new ResponseEntity<>(especialidades, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<especialidadEntity> obtenerEspecialidadPorId(@PathVariable Long id) {
        return especialidadServ.obtenerEspecialidadPorId(id)
                .map(especialidad -> new ResponseEntity<>(especialidad, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PutMapping("/{id}")
    public ResponseEntity<especialidadEntity> actualizarEspecialidad(@PathVariable Long id, @RequestBody especialidadEntity especialidad) {
        return especialidadServ.obtenerEspecialidadPorId(id)
                .map(especialidadExist -> {
                    especialidad.setId_especialidad(id);
                    return new ResponseEntity<>(especialidadServ.actualizarEspecialidad(especialidad), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<especialidadEntity> eliminarEspecialidad(@PathVariable Long id) {
        return especialidadServ.obtenerEspecialidadPorId(id)
                .map(especialidad -> {
                    especialidadServ.eliminarEspecialidad(id);
                    return new ResponseEntity<>(especialidad, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
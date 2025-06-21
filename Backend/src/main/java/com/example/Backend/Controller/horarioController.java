package com.example.Backend.Controller;

import com.example.Backend.DTO.HorarioDTO;
import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Services.horarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class horarioController {
    
    private horarioServices horarioServ;
    
    @Autowired
    public horarioController(horarioServices horarioServ) {
        this.horarioServ = horarioServ;
    }

    @PostMapping("/")
    public ResponseEntity<?>  registrar(@RequestBody horarioEntity horario) {
        try {
            horarioEntity guardado = horarioServ.registrarHorarioValidado(horario);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED); // Paso 5 normal
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Excepción 2
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // Excepción 1 (traslape)
        }
    }
    
    @GetMapping("/")
    public ResponseEntity<List<horarioEntity>> obtenerTodosHorarios() {
        List<horarioEntity> horarios = horarioServ.obtenerTodosHorarios();
        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<horarioEntity> obtenerHorarioPorId(@PathVariable Long id) {
        return horarioServ.obtenerHorarioPorId(id)
                .map(horario -> new ResponseEntity<>(horario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/horario/{id}")
    public ResponseEntity<horarioEntity> actualizarHorario(@PathVariable Long id, @RequestBody horarioEntity horario) {
        return horarioServ.obtenerHorarioPorId(id)
                .map(horarioExist -> {
                    horario.setIdHorario(id);
                    return new ResponseEntity<>(horarioServ.actualizarHorario(horario), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/horario/{id}")
    public ResponseEntity<horarioEntity> eliminarHorario(@PathVariable Long id) {
        return horarioServ.obtenerHorarioPorId(id)
                .map(horario -> {
                    horarioServ.eliminarHorario(id);
                    return new ResponseEntity<>(horario, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Obtener disponibilidad por médico
    @GetMapping("/horario/medico/{idMedico}")
    public ResponseEntity<List<horarioEntity>> listarPorMedico(@PathVariable Long idMedico) {
        return new ResponseEntity<>(horarioServ.obtenerHorariosPorMedico(idMedico), HttpStatus.OK);
    }

    @PostMapping("/crear-multiples")
    public ResponseEntity<?> crearMultiplesHorarios(@RequestBody List<HorarioDTO> bloques) {
        horarioServ.registrarMultiplesHorarios(bloques);
        return ResponseEntity.ok().build();
    }
}
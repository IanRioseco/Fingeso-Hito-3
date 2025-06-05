package com.example.Backend.Controller;

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
    public ResponseEntity<horarioEntity> crearHorario(@RequestBody horarioEntity horario) {
        horarioEntity nuevoHorario = horarioServ.guardarHorario(horario);
        return new ResponseEntity<>(nuevoHorario, HttpStatus.CREATED);
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
    
    @PutMapping("/{id}")
    public ResponseEntity<horarioEntity> actualizarHorario(@PathVariable Long id, @RequestBody horarioEntity horario) {
        return horarioServ.obtenerHorarioPorId(id)
                .map(horarioExist -> {
                    horario.setIdHorario(id);
                    return new ResponseEntity<>(horarioServ.actualizarHorario(horario), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<horarioEntity> eliminarHorario(@PathVariable Long id) {
        return horarioServ.obtenerHorarioPorId(id)
                .map(horario -> {
                    horarioServ.eliminarHorario(id);
                    return new ResponseEntity<>(horario, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
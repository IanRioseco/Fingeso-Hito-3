package com.example.Backend.Controller;

import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Services.pacienteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class pacienteController {

    private final pacienteServices pacienteServ;

    @Autowired
    public pacienteController(pacienteServices pacienteServ) {
        this.pacienteServ = pacienteServ;
    }

    @PostMapping("/")
    public ResponseEntity<?> registrarPaciente(@RequestBody pacienteEntity paciente) {
        try {
            pacienteEntity nuevo = pacienteServ.registrarPaciente(paciente);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<pacienteEntity>> obtenerTodos() {
        return new ResponseEntity<>(pacienteServ.obtenerTodosPacientes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<pacienteEntity> obtenerPorId(@PathVariable Long id) {
        return pacienteServ.obtenerPacientePorId(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<pacienteEntity> actualizar(@PathVariable Long id, @RequestBody pacienteEntity paciente) {
        return pacienteServ.obtenerPacientePorId(id)
                .map(pExist -> {
                    paciente.setRutPa(id);
                    return new ResponseEntity<>(pacienteServ.actualizarPaciente(paciente), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return pacienteServ.obtenerPacientePorId(id)
                .map(p -> {
                    pacienteServ.eliminarPaciente(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


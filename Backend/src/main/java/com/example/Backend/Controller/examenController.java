package com.example.Backend.Controller;

import com.example.Backend.Entity.examenEntity;
import com.example.Backend.Services.examenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examen")
public class examenController {
    private  examenServices examenServ;

    @Autowired
    public examenController(examenServices examenServ) {
        this.examenServ = examenServ;
    }

    @PostMapping("/")
    public ResponseEntity<examenEntity> crearExamen(@RequestBody examenEntity examen) {
        examenEntity nuevoExamen = examenServ.guardarExamen(examen);
        return new ResponseEntity<>(nuevoExamen, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<examenEntity>> obtenerTodosExamenes() {
        List<examenEntity> examenes = examenServ.obtenerTodosExamenes();
        return new ResponseEntity<>(examenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<examenEntity> obtenerExamenPorId(@PathVariable Long id) {
        return examenServ.obtenerExamenPorId(id)
                .map(examen -> new ResponseEntity<>(examen, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<examenEntity> actualizarExamen(@PathVariable Long id, @RequestBody examenEntity examen) {
        return examenServ.obtenerExamenPorId(id)
                .map(examenExist -> {
                    examen.setIdExamen(id);
                    return new ResponseEntity<>(examenServ.actualizarExamen(examen), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<examenEntity> eliminarExamen(@PathVariable Long id) {
        return examenServ.obtenerExamenPorId(id)
                .map(examen -> {
                    examenServ.eliminarExamen(id);
                    return new ResponseEntity<>(examen, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

package com.example.Backend.Controller;

import com.example.Backend.Entity.farmacia_medicamentoEntity;
import com.example.Backend.Services.farmacia_medicamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmacia-medicamento")
public class farmacia_medicamentoController {
    private final farmacia_medicamentoServices farmacia_medicamentoServ;

    @Autowired
    public farmacia_medicamentoController(farmacia_medicamentoServices farmacia_medicamentoServ) {
        this.farmacia_medicamentoServ = farmacia_medicamentoServ;
    }

    @PostMapping("/")
    public ResponseEntity<farmacia_medicamentoEntity> crearFarmaciaMedicamento(@RequestBody farmacia_medicamentoEntity farmaciaMedicamento) {
        farmacia_medicamentoEntity nuevo = farmacia_medicamentoServ.guardarFarmaciaMedicamento(farmaciaMedicamento);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<farmacia_medicamentoEntity>> obtenerTodosFarmaciaMedicamentos() {
        List<farmacia_medicamentoEntity> farmaciaMedicamentos = farmacia_medicamentoServ.obtenerTodosFarmaciaMedicamentos();
        return new ResponseEntity<>(farmaciaMedicamentos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<farmacia_medicamentoEntity> obtenerFarmaciaMedicamentoPorId(@PathVariable Long id) {
        return farmacia_medicamentoServ.obtenerFarmaciaMedicamentoPorId(id)
                .map(farmaciaMedicamento -> new ResponseEntity<>(farmaciaMedicamento, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<farmacia_medicamentoEntity> actualizarFarmaciaMedicamento(
            @PathVariable Long id,
            @RequestBody farmacia_medicamentoEntity farmaciaMedicamento) {
        return farmacia_medicamentoServ.obtenerFarmaciaMedicamentoPorId(id)
                .map(exist -> {
                    farmaciaMedicamento.setId_farmacia_medicamento(id);
                    return new ResponseEntity<>(
                            farmacia_medicamentoServ.actualizarFarmaciaMedicamento(farmaciaMedicamento),
                            HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<farmacia_medicamentoEntity> eliminarFarmaciaMedicamento(@PathVariable Long id) {
        return farmacia_medicamentoServ.obtenerFarmaciaMedicamentoPorId(id)
                .map(farmaciaMedicamento -> {
                    farmacia_medicamentoServ.eliminarFarmaciaMedicamento(id);
                    return new ResponseEntity<>(farmaciaMedicamento, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
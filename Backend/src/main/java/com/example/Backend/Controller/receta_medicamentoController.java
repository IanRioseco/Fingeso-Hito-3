package com.example.Backend.Controller;

import com.example.Backend.DTO.ValidarRecetaRequest;
import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Entity.receta_medicamentoEntity;
import com.example.Backend.Services.receta_medicamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas-medicamentos")
public class receta_medicamentoController {

    private final receta_medicamentoServices recetaServ;

    @Autowired
    public receta_medicamentoController(receta_medicamentoServices recetaServ) {
        this.recetaServ = recetaServ;
    }

    @PostMapping("/")
    public ResponseEntity<receta_medicamentoEntity> crear(@RequestBody receta_medicamentoEntity receta) {
        return new ResponseEntity<>(recetaServ.guardarRecetaMedicamento(receta), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<receta_medicamentoEntity>> obtenerTodas() {
        return new ResponseEntity<>(recetaServ.obtenerTodasRecetasMedicamento(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<receta_medicamentoEntity> obtenerPorId(@PathVariable Long id) {
        return recetaServ.obtenerRecetaMedicamentoPorId(id)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<receta_medicamentoEntity> actualizar(@PathVariable Long id, @RequestBody receta_medicamentoEntity receta) {
        return recetaServ.obtenerRecetaMedicamentoPorId(id)
                .map(rExist -> {
                    receta.setId_recetamedicamento(id);
                    return new ResponseEntity<>(recetaServ.actualizarRecetaMedicamento(receta), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return recetaServ.obtenerRecetaMedicamentoPorId(id)
                .map(r -> {
                    recetaServ.eliminarRecetaMedicamento(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/medicamentos-por-receta/{idReceta}")
    public ResponseEntity<List<medicamentoEntity>> obtenerMedicamentosPorReceta(@PathVariable Long idReceta) {
        List<medicamentoEntity> medicamentos = recetaServ.obtenerMedicamentosPorRecetaId(idReceta);
        return new ResponseEntity<>(medicamentos, HttpStatus.OK);
    }

    @GetMapping("/alertas/farmacia/{farmaciaId}")
    public ResponseEntity<List<recetaEntity>> getRecetasPorFarmacia(@PathVariable Long farmaciaId) {
        List<recetaEntity> recetas = recetaServ.obtenerRecetasPorFarmacia(farmaciaId);
        return new ResponseEntity<>(recetas, HttpStatus.OK);
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validarReceta(@RequestBody ValidarRecetaRequest request) {
        try {
            recetaServ.validarRecetaYDescontarStock(request);
            return ResponseEntity.ok().body("Receta validada y stock descontado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}


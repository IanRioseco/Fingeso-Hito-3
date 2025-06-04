package com.example.Backend.Controller;

import com.example.Backend.Entity.recetaEntity;
import com.example.Backend.Services.recetaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receta")
public class recetaController {
    private final recetaServices recetaServ;

    @Autowired
    public recetaController(recetaServices recetaServ) {
        this.recetaServ = recetaServ;
    }

    @PostMapping("/")
    public ResponseEntity<recetaEntity> crearReceta(@RequestBody recetaEntity receta) {
        recetaEntity nuevaReceta = recetaServ.guardarReceta(receta);
        return new ResponseEntity<>(nuevaReceta, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<recetaEntity>> obtenerTodasRecetas() {
        List<recetaEntity> recetas = recetaServ.obtenerTodasLasRecetas();
        return new ResponseEntity<>(recetas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<recetaEntity> obtenerRecetaPorId(@PathVariable Long id) {
        return recetaServ.obtenerRecetaPorId(id)
                .map(receta -> new ResponseEntity<>(receta, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<recetaEntity> actualizarReceta(@PathVariable Long id, @RequestBody recetaEntity receta) {
        return recetaServ.obtenerRecetaPorId(id)
                .map(recetaExist -> {
                    receta.setId_receta(id);
                    return new ResponseEntity<>(recetaServ.actualizarReceta(receta), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<recetaEntity> eliminarReceta(@PathVariable Long id) {
        return recetaServ.obtenerRecetaPorId(id)
                .map(receta -> {
                    recetaServ.eliminarReceta(id);
                    return new ResponseEntity<>(receta, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

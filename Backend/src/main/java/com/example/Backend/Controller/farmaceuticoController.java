package com.example.Backend.Controller;

import com.example.Backend.Entity.farmaceuticoEntity;
import com.example.Backend.Services.farmaceuticoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmaceuticos")
@CrossOrigin(origins = "http://localhost:5173")
public class farmaceuticoController {

    private final farmaceuticoServices farmaceuticoServ;

    @Autowired
    public farmaceuticoController(farmaceuticoServices farmaceuticoServ) {
        this.farmaceuticoServ = farmaceuticoServ;
    }

    @PostMapping("/")
    public ResponseEntity<farmaceuticoEntity> crearFarmaceutico(@RequestBody farmaceuticoEntity farmaceutico) {
        farmaceuticoEntity nuevoFarmaceutico = farmaceuticoServ.guardarFarmaceutico(farmaceutico);
        return new ResponseEntity<>(nuevoFarmaceutico, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<farmaceuticoEntity>> obtenerTodosFarmaceuticos() {
        List<farmaceuticoEntity> farmaceuticos = farmaceuticoServ.obtenerTodosFarmaceuticos();
        return new ResponseEntity<>(farmaceuticos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<farmaceuticoEntity> obtenerFarmaceuticoPorId(@PathVariable Long id) {
        return farmaceuticoServ.obtenerFarmaceuticoPorId(id)
                .map(farmaceutico -> new ResponseEntity<>(farmaceutico, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<farmaceuticoEntity> actualizarFarmaceutico(@PathVariable Long id, @RequestBody farmaceuticoEntity farmaceutico) {
        return farmaceuticoServ.obtenerFarmaceuticoPorId(id).map(farmaceuticoExist -> {
            farmaceutico.setId_farmaceutico(id);
            return new ResponseEntity<>(farmaceuticoServ.actualizarFarmaceutico(farmaceutico), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<farmaceuticoEntity> eliminarFarmaceutico(@PathVariable Long id) {
        return farmaceuticoServ.obtenerFarmaceuticoPorId(id).map(farmaceutico -> {
            farmaceuticoServ.eliminarFarmaceutico(id);
            return new ResponseEntity<>(farmaceutico, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
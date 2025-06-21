package com.example.Backend.Controller;

import com.example.Backend.Entity.farmaciaEntity;
import com.example.Backend.Services.farmaciaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/farmacia")
public class farmaciaController {
    private farmaciaServices farmaciaServ;

    @Autowired
    public farmaciaController(farmaciaServices farmaciaServ) {
        this.farmaciaServ = farmaciaServ;
    }

    @PostMapping("/")
    public ResponseEntity<farmaciaEntity> crearFarmacia(@RequestBody farmaciaEntity farmacia) {
        farmaciaEntity nuevaFarmacia = farmaciaServ.guardarFarmacia(farmacia);
        return new ResponseEntity<>(nuevaFarmacia, HttpStatus.CREATED);
    }

    @PostMapping("/initialize")
    public ResponseEntity<List<farmaciaEntity>> initializeFarmacias() {
    // Primero limpia las farmacias existentes
    farmaciaServ.eliminarTodasLasFarmacias();

    List<farmaciaEntity> farmaciasCreadas = new ArrayList<>();

    String[] nombres = {
        "Farmacia Central",
        "Farmacia Norte"
    };

    for (String nombre : nombres) {
        farmaciaEntity farmacia = new farmaciaEntity();
        farmacia.setNombre(nombre);
        farmaciasCreadas.add(farmaciaServ.guardarFarmacia(farmacia));
    }

    return ResponseEntity.ok(farmaciasCreadas);
}

    @GetMapping("/")
    public ResponseEntity<List<farmaciaEntity>> obtenerTodasFarmacias() {
        List<farmaciaEntity> farmacias = farmaciaServ.obtenerTodasFarmacias();
        return new ResponseEntity<>(farmacias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<farmaciaEntity> obtenerFarmaciaPorId(@PathVariable Long id) {
        return farmaciaServ.obtenerFarmaciaPorId(id)
                .map(farmacia -> new ResponseEntity<>(farmacia, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<farmaciaEntity> actualizarFarmacia(@PathVariable Long id, @RequestBody farmaciaEntity farmacia) {
        return farmaciaServ.obtenerFarmaciaPorId(id)
                .map(farmaciaExist -> {
                    farmacia.setIdFarmacia(id);
                    return new ResponseEntity<>(farmaciaServ.actualizarFarmacia(farmacia), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<farmaciaEntity> eliminarFarmacia(@PathVariable Long id) {
        return farmaciaServ.obtenerFarmaciaPorId(id)
                .map(farmacia -> {
                    farmaciaServ.eliminarFarmacia(id);
                    return new ResponseEntity<>(farmacia, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
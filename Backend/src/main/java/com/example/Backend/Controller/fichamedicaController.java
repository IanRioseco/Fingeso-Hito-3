package com.example.Backend.Controller;

import com.example.Backend.Entity.farmaciaEntity;
import com.example.Backend.Entity.fichamedicaEntity;
import com.example.Backend.Services.fichamedicaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichamedica")
public class fichamedicaController {

    private fichamedicaServices fichamedicaSer;

    @Autowired
    public fichamedicaController(fichamedicaServices fichamedicaSer){
        this.fichamedicaSer = fichamedicaSer;
    }

    @PostMapping("/")
    public ResponseEntity<fichamedicaEntity> crearFarmacia(@RequestBody fichamedicaEntity fichamedica) {
        fichamedicaEntity nuevaFichamedica = fichamedicaSer.guardarFichaMedica(fichamedica);
        return new ResponseEntity<>(nuevaFichamedica, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<fichamedicaEntity>> obtenerTodasFarmacias() {
        List<fichamedicaEntity> fichasMedicas = fichamedicaSer.obtenerTodasLasFichasMedicas();
        return new ResponseEntity<>(fichasMedicas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> obtenerFarmaciaPorId(@PathVariable Long id) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(fichamedica -> new ResponseEntity<>(fichamedica, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> actualizarFarmacia(@PathVariable Long id, @RequestBody fichamedicaEntity fichamedica) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(fichamedicaExist -> {
                    fichamedica.setId_fichamedica(id);
                    return new ResponseEntity<>(fichamedicaSer.actualizarFichaMedica(fichamedica), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> eliminarFarmacia(@PathVariable Long id) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(farmacia -> {
                    fichamedicaSer.eliminarFichaMedica(id);
                    return new ResponseEntity<>(farmacia, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

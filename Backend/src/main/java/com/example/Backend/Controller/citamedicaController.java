package com.example.Backend.Controller;

import com.example.Backend.Entity.citamedicaEntity;
import com.example.Backend.Services.citamedicaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citamedica")
public class citamedicaController {
    private citamedicaServices citamedicaServ;

    @Autowired
    public citamedicaController(citamedicaServices citamedicaServ) {
        this.citamedicaServ = citamedicaServ;
    }

    @PostMapping("/")
    public ResponseEntity<citamedicaEntity> crearCitamedica(@RequestBody citamedicaEntity citamedica) {
        citamedicaEntity nuevaCita = citamedicaServ.guardarCitamedica(citamedica);
        return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<citamedicaEntity>> obtenerTodasCitasMedicas() {
        List<citamedicaEntity> citas = citamedicaServ.ObtenerLasCitamedica();
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<citamedicaEntity> obtenerCitamedicaPorId(@PathVariable Long id) {
        return citamedicaServ.obtenerCitamedicaPorId(id)
                .map(cita -> new ResponseEntity<>(cita, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<citamedicaEntity> actualizarCitamedica(@PathVariable Long id, @RequestBody citamedicaEntity citamedica) {
        return citamedicaServ.obtenerCitamedicaPorId(id)
                .map(citaExist -> {
                    citamedica.setId_citamedica(id);
                    return new ResponseEntity<>(citamedicaServ.actualizarCitamedica(citamedica), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<citamedicaEntity> eliminarCitamedica(@PathVariable Long id) {
        return citamedicaServ.obtenerCitamedicaPorId(id)
                .map(cita -> {
                    citamedicaServ.eliminarCitamedica(id);
                    return new ResponseEntity<>(cita, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


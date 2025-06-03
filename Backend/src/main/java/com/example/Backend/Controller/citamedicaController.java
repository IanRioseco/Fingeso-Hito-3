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

    @PostMapping
    public ResponseEntity<citamedicaEntity> crearCitamedica(@RequestBody citamedicaEntity citamedica) {
        citamedicaEntity citamedicaEntity = citamedicaServ.guardarCitamedica(citamedica);
        return new ResponseEntity<>(citamedicaEntity, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<citamedicaEntity>> obtenerTodasCitasMedicas(){
        List<citamedicaEntity> citamedica = citamedicaServ.ObtenerLasCitamedica();
        return new ResponseEntity<>(citamedica, HttpStatus.OK);
    }




}

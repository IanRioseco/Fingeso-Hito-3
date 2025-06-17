package com.example.Backend.Controller;

import com.example.Backend.Entity.personaltecnicoEntity;
import com.example.Backend.Services.personaltecnicoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personaltecnico")
@CrossOrigin(origins = "http://localhost:5173")
public class personaltecnicoController {
    private personaltecnicoServices personaltecnicoServ;

    @Autowired
    public personaltecnicoController(personaltecnicoServices personaltecnicoServ) {
        this.personaltecnicoServ = personaltecnicoServ;
    }

    @PostMapping("/")
    public ResponseEntity<personaltecnicoEntity> crearPersonalTecnico(@RequestBody personaltecnicoEntity personaltecnico) {
        personaltecnicoEntity nuevoPersonalTecnico = personaltecnicoServ.guardarPersonalTecnico(personaltecnico);
        return new ResponseEntity<>(nuevoPersonalTecnico, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<personaltecnicoEntity>> obtenerTodosPersonalTecnicos() {
        List<personaltecnicoEntity> personaltecnicos = personaltecnicoServ.obtenerTodosPersonalTecnicos();
        return new ResponseEntity<>(personaltecnicos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<personaltecnicoEntity> obtenerPersonalTecnicoPorId(@PathVariable Long id) {
        return personaltecnicoServ.obtenerPersonalTecnicoPorId(id)
                .map(personaltecnico -> new ResponseEntity<>(personaltecnico, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<personaltecnicoEntity> actualizarPersonalTecnico(@PathVariable Long id, @RequestBody personaltecnicoEntity personaltecnico) {
        return personaltecnicoServ.obtenerPersonalTecnicoPorId(id)
                .map(personaltecnicoExist -> {
                    personaltecnico.setId_pertec(id);
                    return new ResponseEntity<>(personaltecnicoServ.actualizarPersonalTecnico(personaltecnico), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<personaltecnicoEntity> eliminarPersonalTecnico(@PathVariable Long id) {
        return personaltecnicoServ.obtenerPersonalTecnicoPorId(id)
                .map(personaltecnico -> {
                    personaltecnicoServ.eliminarPersonalTecnico(id);
                    return new ResponseEntity<>(personaltecnico, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


package com.example.Backend.Controller;


import com.example.Backend.Entity.personaltecnicoEntity;
import com.example.Backend.Services.personaltecnicoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personaltecnico")
public class personaltecnicoController {

    private final personaltecnicoServices personalServ;

    @Autowired
    public personaltecnicoController(personaltecnicoServices personalServ) {
        this.personalServ = personalServ;
    }

    @PostMapping("/")
    public ResponseEntity<personaltecnicoEntity> crear(@RequestBody personaltecnicoEntity personal) {
        return new ResponseEntity<>(personalServ.guardarPersonalTecnico(personal), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<personaltecnicoEntity>> obtenerTodos() {
        return new ResponseEntity<>(personalServ.obtenerTodosPersonalTecnicos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<personaltecnicoEntity> obtenerPorId(@PathVariable Long id) {
        return personalServ.obtenerPersonalTecnicoPorId(id)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<personaltecnicoEntity> actualizar(@PathVariable Long id, @RequestBody personaltecnicoEntity personal) {
        return personalServ.obtenerPersonalTecnicoPorId(id)
                .map(pExist -> {
                    personal.setId_pertec(id);
                    return new ResponseEntity<>(personalServ.actualizarPersonalTecnico(personal), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return personalServ.obtenerPersonalTecnicoPorId(id)
                .map(p -> {
                    personalServ.eliminarPersonalTecnico(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


package com.example.Backend.Controller;
import com.example.Backend.Entity.recepcionistaEntity;
import com.example.Backend.Services.recepcionistaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionistas")
@CrossOrigin(origins = "http://localhost:5173")
public class recepcionistaController {

    private final recepcionistaServices recepcionistaServ;

    @Autowired
    public recepcionistaController(recepcionistaServices recepcionistaServ) {
        this.recepcionistaServ = recepcionistaServ;
    }

    @PostMapping("/")
    public ResponseEntity<recepcionistaEntity> crearRecepcionista(@RequestBody recepcionistaEntity recepcionista) {
        recepcionistaEntity nuevoRecepcionista = recepcionistaServ.guardarRecepcionista(recepcionista);
        return new ResponseEntity<>(nuevoRecepcionista, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<recepcionistaEntity>> obtenerTodosRecepcionistas() {
        List<recepcionistaEntity> recepcionistas = recepcionistaServ.obtenerTodosRecepcionistas();
        return new ResponseEntity<>(recepcionistas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<recepcionistaEntity> obtenerRecepcionistaPorId(@PathVariable Long id) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(recepcionista -> new ResponseEntity<>(recepcionista, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<recepcionistaEntity> actualizarRecepcionista(@PathVariable Long id, @RequestBody recepcionistaEntity recepcionista) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(recepcionistaExist -> {
                    recepcionista.setIdRecepcionista(id);
                    return new ResponseEntity<>(recepcionistaServ.actualizarRecepcionista(recepcionista), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<recepcionistaEntity> eliminarRecepcionista(@PathVariable Long id) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(recepcionista -> {
                    recepcionistaServ.eliminarRecepcionista(id);
                    return new ResponseEntity<>(recepcionista, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


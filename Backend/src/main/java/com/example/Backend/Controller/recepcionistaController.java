package com.example.Backend.Controller;
import com.example.Backend.Entity.recepcionistaEntity;
import com.example.Backend.Services.recepcionistaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepcionistas")
public class recepcionistaController {

    private final recepcionistaServices recepcionistaServ;

    @Autowired
    public recepcionistaController(recepcionistaServices recepcionistaServ) {
        this.recepcionistaServ = recepcionistaServ;
    }

    @PostMapping("/")
    public ResponseEntity<recepcionistaEntity> crear(@RequestBody recepcionistaEntity r) {
        return new ResponseEntity<>(recepcionistaServ.guardarRecepcionista(r), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<recepcionistaEntity>> obtenerTodos() {
        return new ResponseEntity<>(recepcionistaServ.obtenerTodosRecepcionistas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<recepcionistaEntity> obtenerPorId(@PathVariable Long id) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<recepcionistaEntity> actualizar(@PathVariable Long id, @RequestBody recepcionistaEntity r) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(rExist -> {
                    r.setIdRecepcionista(id);
                    return new ResponseEntity<>(recepcionistaServ.actualizarRecepcionista(r), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return recepcionistaServ.obtenerRecepcionistaPorId(id)
                .map(r -> {
                    recepcionistaServ.eliminarRecepcionista(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


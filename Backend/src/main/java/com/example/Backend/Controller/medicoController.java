package com.example.Backend.Controller;
import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Services.medicoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "http://localhost:5173")
public class medicoController {

    private final medicoServices medicoServ;

    @Autowired
    public medicoController(medicoServices medicoServ) {
        this.medicoServ = medicoServ;
    }

    @PostMapping("/")
    public ResponseEntity<medicoEntity> crearMedico(@RequestBody medicoEntity medico) {
        return new ResponseEntity<>(medicoServ.guardarMedico(medico), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<medicoEntity>> obtenerTodos() {
        return new ResponseEntity<>(medicoServ.obtenerTodosMedicos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<medicoEntity> obtenerPorId(@PathVariable Long id) {
        return medicoServ.obtenerMedicoPorId(id)
                .map(m -> new ResponseEntity<>(m, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<medicoEntity> actualizar(@PathVariable Long id, @RequestBody medicoEntity medico) {
        return medicoServ.obtenerMedicoPorId(id)
                .map(mExist -> {
                    medico.setIdmedico(id);
                    return new ResponseEntity<>(medicoServ.actualizarMedico(medico), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return medicoServ.obtenerMedicoPorId(id)
                .map(m -> {
                    medicoServ.eliminarMedico(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

package com.example.Backend.Controller;

import com.example.Backend.Entity.medicamentoEntity;
import com.example.Backend.Services.medicamentoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamento")
public class medicamentoController {
    
    private medicamentoServices medicamentoServ;
    
    @Autowired
    public medicamentoController(medicamentoServices medicamentoServ) {
        this.medicamentoServ = medicamentoServ;
    }
    
    @PostMapping("/")
    public ResponseEntity<medicamentoEntity> crearMedicamento(@RequestBody medicamentoEntity medicamento) {
        medicamentoEntity nuevoMedicamento = medicamentoServ.guardarMedicamento(medicamento);
        return new ResponseEntity<>(nuevoMedicamento, HttpStatus.CREATED);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<medicamentoEntity>> obtenerTodosMedicamentos() {
        List<medicamentoEntity> medicamentos = medicamentoServ.obtenerTodosMedicamentos();
        return new ResponseEntity<>(medicamentos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<medicamentoEntity> obtenerMedicamentoPorId(@PathVariable Long id) {
        return medicamentoServ.obtenerMedicamentoPorId(id)
                .map(medicamento -> new ResponseEntity<>(medicamento, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<medicamentoEntity> actualizarMedicamento(@PathVariable Long id, @RequestBody medicamentoEntity medicamento) {
        return medicamentoServ.obtenerMedicamentoPorId(id)
                .map(medicamentoExist -> {
                    medicamento.setIdmedicamento(id);
                    return new ResponseEntity<>(medicamentoServ.actualizarMedicamento(medicamento), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<medicamentoEntity> eliminarMedicamento(@PathVariable Long id) {
        return medicamentoServ.obtenerMedicamentoPorId(id)
                .map(medicamento -> {
                    medicamentoServ.eliminarMedicamento(id);
                    return new ResponseEntity<>(medicamento, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
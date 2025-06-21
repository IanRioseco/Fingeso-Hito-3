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
    public ResponseEntity<fichamedicaEntity> crearfichamedica(@RequestBody fichamedicaEntity fichamedica) {
        fichamedicaEntity nuevaFichamedica = fichamedicaSer.guardarFichaMedica(fichamedica);
        return new ResponseEntity<>(nuevaFichamedica, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<fichamedicaEntity>> obtenerTodasfichamedicas() {
        List<fichamedicaEntity> fichasMedicas = fichamedicaSer.obtenerTodasLasFichasMedicas();
        return new ResponseEntity<>(fichasMedicas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> obtenerfichamedicaPorId(@PathVariable Long id) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(fichamedica -> new ResponseEntity<>(fichamedica, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> actualizarfichamedica(@PathVariable Long id, @RequestBody fichamedicaEntity fichamedica) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(fichamedicaExist -> {
                    fichamedica.setId_fichamedica(id);
                    return new ResponseEntity<>(fichamedicaSer.actualizarFichaMedica(fichamedica), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<fichamedicaEntity> eliminarfichamedica(@PathVariable Long id) {
        return fichamedicaSer.obtenerFichaMedicaPorId(id)
                .map(farmacia -> {
                    fichamedicaSer.eliminarFichaMedica(id);
                    return new ResponseEntity<>(farmacia, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<fichamedicaEntity> obtenerFichaMedicaPorPacienteId(@PathVariable Long id) {
        try {
            System.out.println("Buscando ficha médica para paciente ID: " + id);
            fichamedicaEntity ficha = fichamedicaSer.obtenerFichaMedicaPorPacienteId(id);
            if (ficha != null) {
                System.out.println("Ficha médica encontrada: " + ficha.getId_fichamedica());
                return new ResponseEntity<>(ficha, HttpStatus.OK);
            } else {
                System.out.println("No se encontró ficha médica para el paciente ID: " + id);
                // Si no existe la ficha, creamos una nueva
                fichamedicaEntity nuevaFicha = new fichamedicaEntity();
                // Establecer valores por defecto
                nuevaFicha.setEdad(0);
                nuevaFicha.setSexo("");
                nuevaFicha.setSintomas("");
                nuevaFicha.setEnfermedades("");
                nuevaFicha.setPeso(0.0);
                nuevaFicha.setAltura(0.0);
                // Guardar la nueva ficha
                fichamedicaEntity fichaGuardada = fichamedicaSer.guardarFichaMedica(nuevaFicha);
                return new ResponseEntity<>(fichaGuardada, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar ficha médica: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

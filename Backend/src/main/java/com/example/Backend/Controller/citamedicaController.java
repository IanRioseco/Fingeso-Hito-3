package com.example.Backend.Controller;

import com.example.Backend.DTO.CrearCitaRequest;
import com.example.Backend.Entity.citamedicaEntity;
import com.example.Backend.Entity.horarioEntity;
import com.example.Backend.Entity.medicoEntity;
import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Repository.medicoRepository;
import com.example.Backend.Repository.pacienteRepository;
import com.example.Backend.Services.citamedicaServices;
import com.example.Backend.Services.horarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citamedica")
public class citamedicaController {
    private final citamedicaServices citamedicaServ;
    private final horarioServices horarioServ;
    private final medicoRepository medicoRepo;
    private final pacienteRepository pacienteRepo;

    @Autowired
    public citamedicaController(citamedicaServices citamedicaServ, horarioServices horarioServ, medicoRepository medicoRepo, pacienteRepository pacienteRepo) {
        this.citamedicaServ = citamedicaServ;
        this.horarioServ = horarioServ;
        this.medicoRepo = medicoRepo;
        this.pacienteRepo = pacienteRepo;
    }

    @PostMapping("/crear")
    public ResponseEntity<citamedicaEntity> crearCitamedica(@RequestBody CrearCitaRequest request) {
        // Buscar entidades relacionadas
        medicoEntity medico = medicoRepo.findById(request.getIdMedico()).orElse(null);
        pacienteEntity paciente = pacienteRepo.findById(request.getIdPaciente()).orElse(null);
        horarioEntity horario = horarioServ.obtenerHorarioPorId(request.getIdHorario()).orElse(null);
        if (medico == null || paciente == null || horario == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Crear la cita
        citamedicaEntity cita = new citamedicaEntity();
        cita.setEstado(request.getEstado());
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        citamedicaEntity nuevaCita = citamedicaServ.guardarCitamedica(cita);
        // Asociar el horario a la cita
        horario.setCitamedica(nuevaCita);
        horarioServ.actualizarHorario(horario);
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

    @DeleteMapping("/{idCita}")
    public ResponseEntity<Void> eliminarCitamedica(@PathVariable Long idCita) {
        citamedicaServ.eliminarCitamedica(idCita);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<citamedicaEntity>> obtenerCitasPorPaciente(@PathVariable Long idPaciente) {
        List<citamedicaEntity> citas = citamedicaServ.obtenerCitasPorPaciente(idPaciente);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<citamedicaEntity>> obtenerCitasPorMedico(@PathVariable Long idMedico) {
        List<citamedicaEntity> citas = citamedicaServ.obtenerCitasPorMedico(idMedico);
        return new ResponseEntity<>(citas, HttpStatus.OK);
    }


}


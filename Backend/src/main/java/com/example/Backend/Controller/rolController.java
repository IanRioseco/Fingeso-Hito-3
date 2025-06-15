package com.example.Backend.Controller;

import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Services.rolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:5173")
public class rolController {

    @Autowired
    private rolServices rolServices;

    @PostMapping("/initialize")
    public ResponseEntity<List<rolEntity>> initializeRoles() {
        // Primero limpiamos los roles existentes
        rolServices.eliminarTodosLosRoles();

        List<rolEntity> rolesCreados = new ArrayList<>();

        String[] roles = {
                "Administrador",     // ID 1
                "Médico",       // ID 2
                "Técnico",      // ID 3
                "Recepcionista", // ID 4
                "Farmacéutico", // ID 5
                "Paciente" // ID 6
        };

        for (String rolNombre : roles) {
            rolEntity rol = new rolEntity();
            rol.setNombre(rolNombre);
            rolesCreados.add(rolServices.GuardarRol(rol));
        }


        return ResponseEntity.ok(rolesCreados);
    }

@GetMapping("/")
public ResponseEntity<List<rolEntity>> getAllRoles() {
    return ResponseEntity.ok(rolServices.obtenerTodosRoles());
}

@GetMapping("/{id}")
public ResponseEntity<rolEntity> getRolById(@PathVariable Long id) {
    return rolServices.obtenerRolPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

@PostMapping("/")
public ResponseEntity<rolEntity> createRol(@RequestBody rolEntity rol) {
    return ResponseEntity.ok(rolServices.GuardarRol(rol));
}

@PutMapping("/{id}")
public ResponseEntity<rolEntity> updateRol(@PathVariable Long id, @RequestBody rolEntity rol) {
    return rolServices.obtenerRolPorId(id)
            .map(existingRol -> {
                rol.setId_rol(id);
                return ResponseEntity.ok(rolServices.actualizarRol(rol));
            })
            .orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
    return rolServices.obtenerRolPorId(id)
            .map(rol -> {
                rolServices.eliminarRol(id);
                return ResponseEntity.ok().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
}
}


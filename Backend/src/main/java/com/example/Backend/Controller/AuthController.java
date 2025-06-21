package com.example.Backend.Controller;

import com.example.Backend.Entity.pacienteEntity;
import com.example.Backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String rut = credentials.get("rut");
        String password = credentials.get("password");
        String rol = credentials.get("role");

        Object usuario = authService.login(rut, password, rol);
        
        if (usuario != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody pacienteEntity paciente) {
        try {
            pacienteEntity nuevoPaciente = authService.registrarPaciente(paciente);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuario", nuevoPaciente);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            switch (e.getMessage()) {
                case "nombre":
                    response.put("message", "El nombre es requerido");
                    break;
                case "apellido":
                    response.put("message", "El apellido es requerido");
                    break;
                case "fechaNacimiento":
                    response.put("message", "La fecha de nacimiento es requerida");
                    break;
                case "sexo":
                    response.put("message", "El sexo es requerido");
                    break;
                case "rut":
                    response.put("message", "El RUT ya está registrado");
                    break;
                case "correo":
                    response.put("message", "El correo ya está registrado");
                    break;
                case "password":
                    response.put("message", "La contraseña es requerida");
                    break;
                default:
                    response.put("message", "Error al registrar el paciente: " + e.getMessage());
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ex.printStackTrace(); // Mostrar el error real en la consola
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error inesperado: " + ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 
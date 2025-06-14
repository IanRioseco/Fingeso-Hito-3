package com.example.Backend.Controller;

import com.example.Backend.Entity.*;
import com.example.Backend.Services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        try {
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener empleados: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{rut}")
    public ResponseEntity<?> getEmployeeByRut(@PathVariable String rut) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeByRut(rut));
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> registerEmployee(@RequestBody Map<String, Object> employeeData) {
        try {
            Object employee = employeeService.registerEmployee(employeeData);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("employee", employee);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al registrar empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{rut}")
    public ResponseEntity<?> updateEmployee(@PathVariable String rut, @RequestBody Map<String, Object> employeeData) {
        try {
            Object employee = employeeService.updateEmployee(rut, employeeData);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("employee", employee);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String rut) {
        try {
            employeeService.deleteEmployee(rut);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Empleado eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 
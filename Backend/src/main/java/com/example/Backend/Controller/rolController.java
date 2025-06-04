package com.example.Backend.Controller;

import com.example.Backend.Entity.rolEntity;
import com.example.Backend.Services.rolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol")
public class rolController {

    private rolServices rolServices;

    @Autowired
    public rolController(rolServices rolServices) {
        this.rolServices = rolServices;
    }

    @PostMapping("/")
    public ResponseEntity<rolEntity> CrearRol(@RequestBody rolEntity rol) {
        rolEntity nuevorol = rolServices.GuardarRol(rol);
        return new ResponseEntity<>(nuevorol, HttpStatus.CREATED);

    }

    @GetMapping("/")
    public ResponseEntity<List<rolEntity>> ObtenerTodosLosRol() {
        List<rolEntity> roles = rolServices.ObtenerTodoLosRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<rolEntity> ObtenerRolId(@PathVariable Long id) {
        return rolServices.ObtenerRolId(id).map(rol -> new ResponseEntity<>(rol, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<rolEntity> ActualizarRol(@PathVariable Long id, @RequestBody rolEntity rol) {
        return rolServices.ObtenerRolId(id).map(rolExist -> {
            rol.setId_rol(id);
            return new ResponseEntity<>(rolServices.ActualizarRol(rol), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<rolEntity> EliminarRol(@PathVariable Long id) {
        return rolServices.ObtenerRolId(id)
                .map(rol -> {
                    rolServices.eliminarRol(id);
                    return new ResponseEntity<>(rol, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

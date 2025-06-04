package com.example.Backend.Controller;

import com.example.Backend.Entity.administradorEntity;
import com.example.Backend.Services.administradorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*Frontend->Controller->Services->Repository->BD*/

@RestController
@RequestMapping("/api/administrador")
/*localhost:8080/api/administrador*/
public class administradorController {

    private administradorServices administradorServ;

    @Autowired
    public administradorController(administradorServices administradorServ) {
        this.administradorServ = administradorServ;
    }

    @GetMapping("/")
    public ResponseEntity<List<administradorEntity>> ObtenerTodosAdministrador() {
        List<administradorEntity> administrador = administradorServ.ObtenerTodosAdministrador();
        return new ResponseEntity<>(administrador, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<administradorEntity> obtenerPorId(@PathVariable Long id) {
        return administradorServ.obtenerAdministradorPorId(id)
                .map(administrador ->new ResponseEntity<>(administrador, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @PostMapping("/")
    public ResponseEntity<administradorEntity> crearAdministrador(@RequestBody administradorEntity administrador) {
        administradorEntity nuevoAdmin = administradorServ.guardarAdministrador(administrador);
        return new ResponseEntity<>(nuevoAdmin, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<administradorEntity> actualizarAdministrador(@PathVariable Long id, @RequestBody administradorEntity administrador){
        return administradorServ.obtenerAdministradorPorId(id).map(Adminexiste -> {
            administrador.setId_admin(id);
            return new ResponseEntity<>(administradorServ.actualizarAdministrador(administrador), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<administradorEntity> eliminarAdministrador(@PathVariable Long id) {
        return administradorServ.obtenerAdministradorPorId(id)
                .map(administrador ->{
                administradorServ.eliminarAdministrador(id);
                return new ResponseEntity<>(administrador, HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }




}

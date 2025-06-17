package com.example.Backend.Controller;

import com.example.Backend.Entity.tiporeporteEntity;
import com.example.Backend.Services.tiporeporteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tiporeporte")
public class tiporeporteController {

    private tiporeporteServices tiporeporteServ;

    @Autowired
    public tiporeporteController(tiporeporteServices tiporeporteServ) {
        this.tiporeporteServ = tiporeporteServ;
    }

    @PostMapping("/")
    public ResponseEntity<tiporeporteEntity> crearTipoReporte(@RequestBody tiporeporteEntity tiporeporte){
        tiporeporteEntity Nuevotiporeporte = tiporeporteServ.guardarTiporeporte(tiporeporte);
        return new ResponseEntity<>(Nuevotiporeporte, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<tiporeporteEntity>> ObtenerTodosTipoReporte(){
        List<tiporeporteEntity> tiporeporte = tiporeporteServ.ObtenerTodosTiporeporte();
        return new ResponseEntity<>(tiporeporte, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<tiporeporteEntity> obtenerPorId(@PathVariable Long id){
        return tiporeporteServ.obtenerTiporeportePorId(id)
                .map(tiporeporte ->new ResponseEntity<>(tiporeporte, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<tiporeporteEntity> ActualizarPorId(@PathVariable Long id, @RequestBody tiporeporteEntity tiporeporte){
        return tiporeporteServ.obtenerTiporeportePorId(id).map(TipoReporteExist->
        {tiporeporte.setId_tipodereporte(id);
        return new ResponseEntity<>(tiporeporteServ.actualizarTiporeporte(tiporeporte), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<tiporeporteEntity> EliminarPorId(@PathVariable Long id){
        return  tiporeporteServ.obtenerTiporeportePorId(id).map(tiporeporte->{
            tiporeporteServ.eliminarTiporeporte(id);
            return new ResponseEntity<>(tiporeporte, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
//
package com.example.Backend.Controller;

import com.example.Backend.Entity.reporteEntity;
import com.example.Backend.Services.reporteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reporte")
public class reporteController {

    private reporteServices reporteServices;

    @Autowired
    public reporteController(reporteServices reporteServices) {
        this.reporteServices = reporteServices;
    }

    @PostMapping("/")
    public ResponseEntity<reporteEntity> crearReporte(@RequestBody reporteEntity reporte){
        reporteEntity nuevoReporte = reporteServices.guardarReporte(reporte);
        return new ResponseEntity<>(nuevoReporte, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<reporteEntity>>obtenerTodosReportes(){
         List<reporteEntity> reportes = reporteServices.obtenerTodosLosReportes();
         return new ResponseEntity<>(reportes, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<reporteEntity> obtenerReportePorId(@PathVariable Long id){
        return reporteServices.obtenerReportePorId(id)
                .map(reporte ->new ResponseEntity<>(reporte, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<reporteEntity> ActualizarReporte(@PathVariable Long id, @RequestBody reporteEntity reporte){
        return reporteServices.obtenerReportePorId(id).map(ReporteExist->
        {reporte.setId_reporte(id);
        return new ResponseEntity<>(reporteServices.actualizarReporte(reporte), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<reporteEntity> EliminarReporte(@PathVariable Long id){
        return reporteServices.obtenerReportePorId(id).map(reporte->{
            reporteServices.eliminarReporte(id);
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

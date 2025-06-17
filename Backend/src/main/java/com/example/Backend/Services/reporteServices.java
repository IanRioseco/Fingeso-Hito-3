package com.example.Backend.Services;


import com.example.Backend.Entity.reporteEntity;
import com.example.Backend.Repository.reporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class reporteServices{
    private reporteRepository reporteRepo;

    @Autowired
    public reporteServices(reporteRepository reporteRepo) {
        this.reporteRepo = reporteRepo;
    }

    public reporteEntity guardarReporte(reporteEntity reporte) {
        return reporteRepo.save(reporte);
    }

    public List<reporteEntity> obtenerTodosLosReportes() {
        return reporteRepo.findAll();
    }

    public Optional<reporteEntity> obtenerReportePorId(Long id) {
        return reporteRepo.findById(id);
    }
    public void eliminarReporte(Long Id) {
        reporteRepo.deleteById(Id);
    }

    public reporteEntity actualizarReporte(reporteEntity reporte) {
        return reporteRepo.save(reporte);
    }
}

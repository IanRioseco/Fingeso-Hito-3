package com.example.Backend.DTO;

public class HorarioDTO {
    private String fecha; // formato: "yyyy-MM-dd"
    private String horainicio; // formato: "HH:mm"
    private String horafin;    // formato: "HH:mm"
    private Long idMedico;

    // Getters y setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHorainicio() { return horainicio; }
    public void setHorainicio(String horainicio) { this.horainicio = horainicio; }
    public String getHorafin() { return horafin; }
    public void setHorafin(String horafin) { this.horafin = horafin; }
    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }
}
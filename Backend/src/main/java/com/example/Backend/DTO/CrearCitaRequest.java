package com.example.Backend.DTO;

public class CrearCitaRequest {
    private String estado;
    private Long idMedico;
    private Long idPaciente;
    private Long idHorario;
    private Long id_fichamedica;

    // Getters y setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    public Long getIdHorario() { return idHorario; }
    public void setIdHorario(Long idHorario) { this.idHorario = idHorario; }
    public Long getId_fichamedica() { return id_fichamedica; }
    public void setId_fichamedica(Long id_fichamedica) { this.id_fichamedica = id_fichamedica; }
}

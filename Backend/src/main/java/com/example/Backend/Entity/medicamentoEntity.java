package com.example.Backend.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "medicamentos")
public class medicamentoEntity {

    @Id
    @Column(name = "ID_medicamento")
    private Long id;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "cantidad")
    private Long cantidad;

    @Column(name = "fecha_vencimieto", length = 50)
    private String fecha_vencimieto;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;
}

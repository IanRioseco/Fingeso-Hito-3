package com.example.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Recepcionistas")
public class recepcionistaEntity {

    @Id
    @Column(name = "ID_recepcionista")
    private Long IdRecepcionista;

    @Column(name = "nombre", length = 50)
    private String Nombre;

    @Column(name = "apellido", length = 50)
    private String Apellido;

    @Column(name = "correo", length = 20)
    private String Correo;

    @Column(name = "telefono", length = 10)
    private String Telefono;

}

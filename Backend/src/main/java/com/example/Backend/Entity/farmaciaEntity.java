package com.example.Backend.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Farmacias")
public class farmaciaEntity {

    @Id
    @Column(name = "Id_farmacia")
    private Long idFarmacia;

    @Column(name = "nombre", length = 50)
    private String nombre;

}

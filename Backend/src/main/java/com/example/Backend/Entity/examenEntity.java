package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "examen")
public class examenEntity {

    @Id
    @Column(name = "Id_examen")
    private Long IdExamen;

    @Column(name = "fecha")
    private Date fecha;

    @OneToMany()
}

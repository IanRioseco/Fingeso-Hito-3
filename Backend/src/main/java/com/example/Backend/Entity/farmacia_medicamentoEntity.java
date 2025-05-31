package com.example.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "farmacia_medicamento")
public class farmacia_medicamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_farmacia_medicamento")
    private Long Id_farmacia_medicamento;

    @ManyToOne
    @JoinColumn(name = "Id_farmacia", referencedColumnName = "Id_farmacia")
    private farmaciaEntity farmacia;

    @ManyToOne
    @JoinColumn(name = "Id_medicamento", referencedColumnName = "Id_medicamento")
    private medicamentoEntity medicamento;
}

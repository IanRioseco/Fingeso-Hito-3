package com.example.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "receta_medicamentoEntity")
public class receta_medicamentoEntity {

    @Id
    @Column(name = "ID_farmacia")
    private Long idFarmacia;




}

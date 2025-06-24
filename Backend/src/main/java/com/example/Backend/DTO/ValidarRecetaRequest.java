package com.example.Backend.DTO;

import lombok.Data;
import java.util.List;

@Data
public class ValidarRecetaRequest {
    private Long idReceta;
    private List<DetalleEntrega> detalles;

    @Data
    public static class DetalleEntrega {
        private Long idMedicamento;
        private Integer cantidadEntregar;
    }
}

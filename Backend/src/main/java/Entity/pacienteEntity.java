package Entity;


import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class pacienteEntity {
    @Id
    @Column(name = "rut_paciente", length = 10)
    private String rutPa;

    @Column(name = "nombre_paciente", length = 80)
    private String nombrePa;

    @Column(name = "apellido_paciente", length = 80)
    private String apellidoPa;
}

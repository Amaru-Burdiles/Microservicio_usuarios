package duoc.amaru.usuarios.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "empleado")
public class Empleado extends Usuario {
    
    @Column(name = "cargo_emp")
    @NotBlank(message = "Cargo obligatorio")
    private String cargo;

    @Column(name = "hire_date_emp")
    private LocalDate hirDate;

}

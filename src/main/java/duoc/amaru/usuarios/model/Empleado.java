package duoc.amaru.usuarios.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "empleado")
public class Empleado extends Usuario {
    
    @Column(name = "cargo_emp")
    @NotBlank(message = "Cargo obligatorio")
    private String cargo;

    @Column(name = "hire_date_emp")
    private LocalDate hirDate;

    public Empleado(Long id,
                    String rut,
                    String pNomb,
                    String sNomb,
                    String pApel,
                    String sApel,
                    String mail,
                    String password,
                    String telefono,
                    String estado,
                    int nvlPermiso,
                    String cargo,
                    LocalDate hirDate) {
        super(id, rut, pNomb, sNomb, pApel, sApel, mail, password, telefono, estado, nvlPermiso);
        this.cargo = cargo;
        this.hirDate = hirDate;
    }

    public Empleado() {
        super();
    }

}

package duoc.amaru.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "etiqueta_dir")
    @Size(max = 30)
    private String etiqueta;

    @Column(name = "calle_dir")
    @NotBlank
    private String calle;

    @Column(name = "nCalle_dir")
    private int nCalle;

    @Column(name = "nCasaDpto_dir")
    @NotBlank
    @Min(value = 1)
    @Max(value = 9999)
    private int nCasaDpto;

    @Column(nullable = true, name = "detalle_dir")
    private String detalle;

    @Column(nullable = false, name = "comuna_dir")
    private Comuna comuna;

    @Column(nullable = false, name = "region_dir")
    private Region region;
}

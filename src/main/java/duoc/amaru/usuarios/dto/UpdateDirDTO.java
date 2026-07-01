package duoc.amaru.usuarios.dto;

import duoc.amaru.usuarios.enums.Comuna;
import duoc.amaru.usuarios.enums.Region;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDirDTO {
    private String etiqueta;
    private String calle;
    private int numCalle;

    @Min(value = 0, message = "El número de casa o departamento debe ser al menos 1")
    @Max(value = 9999, message = "El número de casa o departamento no puede exceder 9999")
    private int numCasaDpto;
    private String detalle;
    private Comuna comuna;
    private Region region;
}

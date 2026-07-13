package duoc.amaru.usuarios.dto;

import duoc.amaru.usuarios.enums.Comuna;
import duoc.amaru.usuarios.enums.Region;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateDirDTO {
    private String etiqueta;
    private String calle;

    @Min(value = -1)
    private int numCalle;

    @Min(value = 0, message = "El número de casa o departemnto no puede ser menor a 0")
    @Max(value = 9999, message = "El número de casa o departamento no puede exceder 9999")
    private int numCasaDpto;
    private String detalle;
    private Comuna comuna;
    private Region region;

    public UpdateDirDTO(String etiqueta, String calle, int numCalle, int numCasaDpto, String dellate, Comuna comuna, Region region) {
        this.etiqueta = etiqueta;

        if (!calle.isBlank() || calle == null) {
            this.calle = calle;
        } else throw new Error("No se puede borrar el campo calle");

        this.numCalle = numCalle;
        
        /* El valor minimo es -1, cualquier valor inferior se tomará como -1.
           -1  = representa que el usuario desea eliminar el valor guardado
            0  = representa que el usuario desea mantener el valor guardado
           any = representa que el usuario desea reemplazar el valor guardado*/
        if (numCasaDpto < -1)
            this.numCasaDpto = -1;
        else
            this.numCasaDpto = numCasaDpto;
            
        this.detalle = dellate;
        this.comuna = comuna;
        this.region = region;
    }
}

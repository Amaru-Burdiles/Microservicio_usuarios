package duoc.amaru.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetDireccionDTO {
    private Long idDireccion;
    private String etiqueta;
}

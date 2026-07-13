package duoc.amaru.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetClientesDTO {
    private Long idCliente;
    private String nombreApellido;
    private String correo;
    private String estado;
    private int nvlPermiso;
}

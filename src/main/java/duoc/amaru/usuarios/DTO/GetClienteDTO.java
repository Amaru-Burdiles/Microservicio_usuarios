package duoc.amaru.usuarios.dto;

import java.util.List;

import duoc.amaru.usuarios.model.Direccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetClienteDTO {
    
    // Vista cliente + Admin
    private Long idCliente;
    private String correo;
    private String telefono;
    
    // Vista solo cliente
    private String rut;
    private List<Direccion> direcciones;
    private String nombreCompleto;
    private String password;
    
    // Vista solo Admin
    private String nombre;
    private String apellido;
    private String estato;
    private String nvlPermiso;
}

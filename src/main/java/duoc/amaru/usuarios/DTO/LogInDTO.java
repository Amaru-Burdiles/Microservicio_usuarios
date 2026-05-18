package duoc.amaru.usuarios.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInDTO {
    private String correo;
    private String password;

    /*
    {
        "correo": "user@example.com",
        "password": "password123"
    }
    */
}

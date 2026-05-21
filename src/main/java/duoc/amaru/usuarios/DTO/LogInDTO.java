package duoc.amaru.usuarios.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInDTO {
    @NotBlank(message = "Falta el correo")
    private String correo;

    @NotBlank(message = "Falta la contraseña")
    private String password;

    /*
    {
        "correo": "user@example.com",
        "password": "password123"
    }
    */
}

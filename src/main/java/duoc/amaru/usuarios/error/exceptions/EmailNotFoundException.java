package duoc.amaru.usuarios.error.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("El correo ingresado no está registrado");
    }
}

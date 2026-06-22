package duoc.amaru.usuarios.error.exceptions;

public class NotSignedInException extends RuntimeException {
    public NotSignedInException() {
        super("Usuario no registrado");
    }
}

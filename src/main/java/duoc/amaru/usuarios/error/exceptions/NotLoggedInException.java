package duoc.amaru.usuarios.error.exceptions;

public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("Usuario no autenticado. Se requiere iniciar sesión");
    }
}

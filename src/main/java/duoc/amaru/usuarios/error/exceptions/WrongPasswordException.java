package duoc.amaru.usuarios.error.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("La contraseña es incorrecta");
    }
}
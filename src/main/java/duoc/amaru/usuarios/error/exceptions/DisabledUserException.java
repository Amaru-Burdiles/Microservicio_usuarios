package duoc.amaru.usuarios.error.exceptions;

public class DisabledUserException extends RuntimeException {
    public DisabledUserException() {
        super("Esta cuenta se encuentra desactivada");
    }
}

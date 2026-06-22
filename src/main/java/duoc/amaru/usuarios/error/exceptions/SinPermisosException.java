package duoc.amaru.usuarios.error.exceptions;

public class SinPermisosException extends RuntimeException {
    public SinPermisosException() {
        super("Permisos insuficientes");
    }
}

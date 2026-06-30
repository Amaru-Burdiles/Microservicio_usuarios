package duoc.amaru.usuarios.error.exceptions;

public class SinCambiosException extends RuntimeException {
    public SinCambiosException() {
        super("No hay cambios para actualizar");
    }
}

package duoc.amaru.usuarios.error.exceptions;

public class ClientesOnlyException extends RuntimeException {
    public ClientesOnlyException() {
        super("Solo clientes pueden realizar esta acción");
    }
}

package duoc.amaru.usuarios.error.exceptions;

public class EmployeesOnlyException extends RuntimeException {
    public EmployeesOnlyException() {
        super("Solo empleados pueden realizar esta acción");
    }
}

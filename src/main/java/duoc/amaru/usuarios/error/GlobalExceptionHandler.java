package duoc.amaru.usuarios.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import duoc.amaru.usuarios.error.exceptions.ClientesOnlyException;
import duoc.amaru.usuarios.error.exceptions.EmployeesOnlyException;
import duoc.amaru.usuarios.error.exceptions.NotLoggedInException;
import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.error.exceptions.SinCambiosException;
import duoc.amaru.usuarios.error.exceptions.SinPermisosException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejoErroresValidacion(MethodArgumentNotValidException ex) {
        @SuppressWarnings("unused")
        Map<String, String> errores = new HashMap<>();

        for (var error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        return errores;
    }
    // EXCEPCIONES DE SERVICIO SESIÓN
    @ExceptionHandler(ClientesOnlyException.class)
    public ResponseEntity<String> validSoloClientes(ClientesOnlyException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler(EmployeesOnlyException.class)
    public ResponseEntity<String> validSoloEmpleados(EmployeesOnlyException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<String> validLoggedIn(NotLoggedInException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(NotSignedInException.class)
    public ResponseEntity<String> validSignedIn(NotSignedInException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(SinPermisosException.class)
    public ResponseEntity<String> validPermisos(SinPermisosException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }

    // EXCEPCIÓN DE SERVICIO CLIENTE
    @ExceptionHandler(SinCambiosException.class)
    public ResponseEntity<String> sinCambios(SinCambiosException ex) {
        return ResponseEntity.badRequest().body("No hay cambios para actualizar");
    }
}
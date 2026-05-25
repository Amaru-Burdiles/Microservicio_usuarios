package duoc.amaru.usuarios.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.EmpleadoRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class SesionServicio {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private EmpleadoRepo empleadoRepo;

    @Autowired
    private ClienteRepo clienteRepo;

    // Id de usuarios logeados
    private Set<Long> sesiones = new HashSet<>();

    public boolean isLoggedIn(Long id) {
        return sesiones.contains(id);
    }

    public boolean logIn(Long id) {
        return sesiones.add(id);
    }

    public boolean logOut(Long id) {
        return sesiones.remove(id);
    }

    // VALIDAR NIVEL DE ACCESO EMPLEADOS
    public ResponseEntity<?> accessValidation(Long executorId, int lvlFilter) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            return ResponseEntity.status(400).body("Usuario no registrado");

        // Valida que se trata de un empleado
        if (!empleadoRepo.existsById(executorId))
            return ResponseEntity.status(403).body("Solo empleados pueden realizar esta acción");

        // Valida que el empleado ha iniciado sesión
        if (!isLoggedIn(executorId))
            return ResponseEntity.status(401).body("Usuario no autenticado. Se requiere iniciar sesión");

        // Valida que el empleado tiene permisos suficientes
        Empleado emp = empleadoRepo.getReferenceById(executorId);
        if (emp.getNvlPermiso() < lvlFilter)
            return ResponseEntity.status(403).body("Permisos insuficientes");

        return null;
    }

    // VALIDAR CLIENTES
    public ResponseEntity<?> validacionCliente(Long executorId) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            return ResponseEntity.status(400).body("Usuario no registrado");

        // Valida que se trata de un cliente
        if (!clienteRepo.existsById(executorId))
            return ResponseEntity.status(403).body("Solo clientes pueden realizar esta acción");

        // Valida que el cliente ha iniciado sesión
        if (!isLoggedIn(executorId))
            return ResponseEntity.status(401).body("Usuario no autenticado. Se requiere iniciar sesión");
        
        return null;
    }
}

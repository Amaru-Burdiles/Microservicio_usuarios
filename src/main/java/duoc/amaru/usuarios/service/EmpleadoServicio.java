package duoc.amaru.usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.repository.EmpleadoRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class EmpleadoServicio {
    @Autowired
    private EmpleadoRepo empleadoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private SesionServicio sesionServicio;

    // REGISTRO DE EMPLEADOS
    public ResponseEntity<?> registrarUsuario(Long id, Empleado newEmpleado) {
        // # VALIDACION DE PERMISOS
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(id))
            return ResponseEntity.status(400).body("Usuario no registrado");

        // Valida que se trata de un empleado
        if (!empleadoRepo.existsById(id))
            return ResponseEntity.status(403).body("Solo empleados pueden realizar esta acción");

        // Valida que el empleado ha iniciado sesión
        Empleado emp = empleadoRepo.getReferenceById(id);
        if (!sesionServicio.isLoggedIn(id))
            return ResponseEntity.status(401).body("Usuario no autenticado. Se requiere iniciar sesión");

        // Valida que el empleado tiene permisos suficientes (3, 4)
        if (emp.getNvlPermiso() < 3)
            return ResponseEntity.status(403).body("Permisos insuficientes");

        // # VALIDACION DE EMPLEADO NUEVO
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(newEmpleado.getCorreo()))
            return ResponseEntity.status(400).body("El correo ingresado ya está registrado");

        // Config usuario nuevo
        newEmpleado.setEstado("activo");
        evaluarCargo(newEmpleado.getCargo(), newEmpleado);

        // Guardar usuario nuevo y respuesta al controlador
        usuarioRepo.save(newEmpleado);
        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }

    
    // CONFIG DE PERMISOS Y CARGO
    private void evaluarCargo(String cargo, Empleado emp) {
        if (cargo.equalsIgnoreCase("admin")) {
            emp.setCargo("Admin");
            emp.setNvlPermiso(4);
            return;
        }

        if (cargo.equalsIgnoreCase("gerente")) {
            emp.setCargo("Gerente");
            emp.setNvlPermiso(3);
            return;
        }

        emp.setCargo("Encargado de Ventas");
        emp.setNvlPermiso(2);
    }
}

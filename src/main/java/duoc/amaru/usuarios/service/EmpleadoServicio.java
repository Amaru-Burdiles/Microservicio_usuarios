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

    // REGISTRO DE EMPLEADOS
    public ResponseEntity<?> registrarUsuario(Empleado newEmpleado) {
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(newEmpleado.getCorreo()))
            return ResponseEntity.status(400).body("El correo ingresado ya está registrado");

        // Config usuario nuevo
        newEmpleado.setEstado("activo");
        evaluarCargo(newEmpleado.getCargo(), newEmpleado);

        // Guardar usuario nuevo
        usuarioRepo.save(newEmpleado);
        return ResponseEntity.ok("Usuario registrado exitosamente");
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

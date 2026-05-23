package duoc.amaru.usuarios.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.model.Usuario;
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

    // Niveles de acceso permitidos
    private Set<Integer> allowedLvls = Set.of(1, 2, 3, 4);

    // Cargos aceptados
    private Set<String> allowedCargos = Set.of("admin", "gerente", "encargado de ventas");

    
    
    // REGISTRO DE EMPLEADOS
    public ResponseEntity<?> registrarUsuario(Long id, Empleado newEmpleado) {
        // # VALIDACION DE PERMISOS
        ResponseEntity<?> reply = accessValidation(id, 3);
        if (reply != null)
            return reply;

        // # VALIDACION DE EMPLEADO NUEVO
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(newEmpleado.getCorreo()))
            return ResponseEntity.status(400).body("El correo ingresado ya está registrado");

        // Config usuario nuevo por defecto
        newEmpleado.setEstado("activo");
        evaluarCargo(newEmpleado.getCargo(), newEmpleado);

        // Guardar usuario nuevo y respuesta al controlador
        usuarioRepo.save(newEmpleado);
        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }

    // ACTUALIZAR PERMISOS DE USUARIO
    public ResponseEntity<?> updateUsusario(Long userId, int nvl, Long executor) {
        // Validar permisos del ejecutor
        ResponseEntity<?> reply = accessValidation(executor, 4);
        if (reply != null)
            return reply;

        // Validar que el usuario a actualizar existe
        Optional<Usuario> updating = usuarioRepo.findById(userId);
        if (!updating.isPresent())
            return ResponseEntity.status(404).body("No se hayó usuario con Id #"+ userId);

        // Validar nuevo nvl
        if (!allowedLvls.contains(nvl))
            return ResponseEntity.status(400).body("Nuevo nivel de acceso fuera de rango");

        Usuario user = updating.get();
        user.setNvlPermiso(nvl);
        return ResponseEntity.ok("Permisos actualizados para usuario "+ user.getPNombre());
    }

    // ACTUALIZAR CARGO EMPLEADO
    public ResponseEntity<?> updateUsuario(Long empId, String cargo, Long executor) {
        // Validar permisos del ejecutor
        ResponseEntity<?> reply = accessValidation(executor, 4);
        if (reply != null)
            return reply;

        // Validar que empleado a actualizar existe
        Optional<Empleado> updating = empleadoRepo.findById(empId);
        if (!updating.isPresent())
            return ResponseEntity.status(404).body("No se hayó empleado con Id #"+ empId);

        // Validar nuevo cargo
        if (!allowedCargos.contains(cargo.toLowerCase()))
            return ResponseEntity.status(400).body("Cargo inválido");

        Empleado emp = updating.get();
        emp.setCargo(cargo);
        return ResponseEntity.ok("Cargo actualizado para empleado "+ emp.getPNombre());
    }
    
    // DESACTIVAR USUARIOS
    public ResponseEntity<?> desactivarUser(Long userId, Long executorId) {
        // Validar permisos del ejecutor
        ResponseEntity<?> reply = accessValidation(executorId, 4);
        if (reply != null)
            return reply;

        // Validar que usuario a desactivar existe
        Optional<Usuario> disabling = usuarioRepo.findById(userId);
        if (!disabling.isPresent())
            return ResponseEntity.status(404).body("No se hayó usuario con Id #"+ userId);

        Usuario user = disabling.get();
        user.setEstado("Desactivado");
        sesionServicio.logOut(userId);
        return ResponseEntity.ok("La cuenta de "+ user.getPNombre() +" ha sido desactivada"); 
    }

    // ELIMINAR USUARIOS
    public ResponseEntity<?> eliminarUser(Long userId, Long executorId) {
        // Validar permisos del ejecutor
        ResponseEntity<?> reply = accessValidation(executorId, 4);
        if (reply != null)
            return reply;

        // Validar que usuario a desactivar existe
        Optional<Usuario> deleting = usuarioRepo.findById(userId);
        if (!deleting.isPresent())
            return ResponseEntity.status(404).body("No se hayó usuario con Id #"+ userId);

        usuarioRepo.delete(deleting.get());
        return ResponseEntity.ok("Usuario con Id #"+ userId +" eliminado");
    }

    // AUTO CONFIG DE PERMISOS Y CARGO
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

    // VALIDAR NIVEL DE ACCESO
    private ResponseEntity<?> accessValidation(Long executorId, int lvlFilter) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            return ResponseEntity.status(400).body("Usuario no registrado");

        // Valida que se trata de un empleado
        if (!empleadoRepo.existsById(executorId))
            return ResponseEntity.status(403).body("Solo empleados pueden realizar esta acción");

        // Valida que el empleado ha iniciado sesión
        Empleado emp = empleadoRepo.getReferenceById(executorId);

        if (!sesionServicio.isLoggedIn(executorId))
            return ResponseEntity.status(401).body("Usuario no autenticado. Se requiere iniciar sesión");

        // Valida que el empleado tiene permisos suficientes
        if (emp.getNvlPermiso() < lvlFilter)
            return ResponseEntity.status(403).body("Permisos insuficientes");

        return null;
    }



}

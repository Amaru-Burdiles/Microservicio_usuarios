package duoc.amaru.usuarios.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
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
    public Empleado registrarUsuario(Long id, Empleado newEmpleado) {
        // # VALIDACION DE PERMISOS
        sesionServicio.validacionEmpleado(id, 3);

        // # VALIDACION DE EMPLEADO NUEVO
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(newEmpleado.getCorreo()))
            return null;

        // Config usuario nuevo por defecto
        newEmpleado.setEstado("Activo");
        evaluarCargo(newEmpleado.getCargo(), newEmpleado);

        // Guardar usuario nuevo y respuesta al controlador
        return usuarioRepo.save(newEmpleado);
    }

    // ACTUALIZAR PERMISOS DE USUARIO
    public Usuario updateUsuario(Long userId, int nvl, Long executor) {
        // Validar permisos del ejecutor
        sesionServicio.validacionEmpleado(executor, 4);

        // Validar que el usuario a actualizar existe
        Optional<Usuario> updating = usuarioRepo.findById(userId);
        if (!updating.isPresent())
            throw new NotSignedInException();

        // Validar nuevo nvl
        if (!allowedLvls.contains(nvl))
            return null;

        Usuario user = updating.get();
        user.setNvlPermiso(nvl);
        return usuarioRepo.save(user);
    }

    // ACTUALIZAR CARGO EMPLEADO
    public Empleado updateUsuario(Long empId, String cargo, Long executor) {
        // Validar permisos del ejecutor
        sesionServicio.validacionEmpleado(executor, 4);

        // Validar que empleado a actualizar existe
        Optional<Empleado> updating = empleadoRepo.findById(empId);
        if (!updating.isPresent())
            throw new NotSignedInException();

        // Validar nuevo cargo
        if (!allowedCargos.contains(cargo.toLowerCase()))
            return null;

        Empleado emp = updating.get();
        emp.setCargo(cargo);
        return empleadoRepo.save(emp);
    }
    
    // DESACTIVAR USUARIOS
    public Usuario desactivarUser(Long userId, Long executorId) {
        // Validar permisos del ejecutor
        sesionServicio.validacionEmpleado(executorId, 4);

        // Validar que usuario a desactivar existe
        Optional<Usuario> disabling = usuarioRepo.findById(userId);
        if (!disabling.isPresent())
            throw new NotSignedInException();

        Usuario user = disabling.get();
        user.setEstado("desactivado");
        sesionServicio.logOut(userId);
        return usuarioRepo.save(user);
    }

    // ELIMINAR USUARIOS
    public boolean eliminarUser(Long userId, Long executorId) {
        // Validar permisos del ejecutor
        sesionServicio.validacionEmpleado(executorId, 4);

        // Validar que usuario a desactivar existe
        Optional<Usuario> deleting = usuarioRepo.findById(userId);
        if (!deleting.isPresent())
            return false;

        usuarioRepo.delete(deleting.get());
        return true;
    }

    // AUTO CONFIG DE PERMISOS Y CARGO EN CREACIÓN DE EMPLEADO
    private void evaluarCargo(String cargo, Empleado emp) {
        if (cargo.equalsIgnoreCase("admin")) {
            emp.setCargo("admin");
            emp.setNvlPermiso(4);
            return;
        }

        if (cargo.equalsIgnoreCase("gerente")) {
            emp.setCargo("gerente");
            emp.setNvlPermiso(3);
            return;
        }

        // cargo por defecto
        emp.setCargo("encargado de ventas");
        emp.setNvlPermiso(2);
    }


}

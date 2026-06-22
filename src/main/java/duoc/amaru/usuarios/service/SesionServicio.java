package duoc.amaru.usuarios.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.error.exceptions.ClientesOnlyException;
import duoc.amaru.usuarios.error.exceptions.EmployeesOnlyException;
import duoc.amaru.usuarios.error.exceptions.NotLoggedInException;
import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.error.exceptions.SinPermisosException;
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

    // VALIDAR SESION
    public boolean isLoggedIn(Long id) {
        return sesiones.contains(id);
    }

    // INICIAR SESION
    public boolean logIn(Long id) {
        return sesiones.add(id);
    }

    // CERRAR SESION
    public boolean logOut(Long id) {
        return sesiones.remove(id);
    }

    // VALIDAR USUARIOS
    public boolean validacionUsuario(Long executorId) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            throw new NotSignedInException();

        // Valida que el usuario ha iniciado sesión
        if (!isLoggedIn(executorId))
            throw new NotLoggedInException();        

        return true;
    }

    // VALIDAR NIVEL DE ACCESO EMPLEADOS
    public boolean validacionEmpleado(Long executorId, int lvlFilter) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            throw new NotSignedInException();

        // Valida que se trata de un empleado
        if (!empleadoRepo.existsById(executorId))
            throw new EmployeesOnlyException();

        // Valida que el empleado ha iniciado sesión
        if (!isLoggedIn(executorId))
            throw new NotLoggedInException();

        // Valida que el empleado tiene permisos suficientes
        Empleado emp = empleadoRepo.getReferenceById(executorId);
        if (emp.getNvlPermiso() < lvlFilter)
            throw new SinPermisosException();

        return true;
    }

    // VALIDAR CLIENTES
    public boolean validacionCliente(Long executorId) {
        // Valida que el Id de usuario existe
        if (!usuarioRepo.existsById(executorId))
            throw new NotSignedInException();

        // Valida que se trata de un cliente
        if (!clienteRepo.existsById(executorId))
            throw new ClientesOnlyException();

        // Valida que el cliente ha iniciado sesión
        if (!isLoggedIn(executorId))
            throw new NotLoggedInException();

        return true;
    }
}

// Validar si el usuario esta desactivado

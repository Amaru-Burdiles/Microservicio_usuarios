package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.error.exceptions.ClientesOnlyException;
import duoc.amaru.usuarios.error.exceptions.EmployeesOnlyException;
import duoc.amaru.usuarios.error.exceptions.NotLoggedInException;
import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.error.exceptions.SinPermisosException;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.EmpleadoRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class SesionServicioTest {
    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private EmpleadoRepo empleadoRepo;

    @Mock
    private ClienteRepo clienteRepo;

    @InjectMocks
    private SesionServicio sesionServicio;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    // TEST INICIAR SESION (EXITOSO)
    @Test
    void testIniciarSesion() {
        // Testeo
        boolean resultado = sesionServicio.logIn(1L);

        // Validación
        assertTrue(resultado);
    }


    // TEST INICIAR SESION (FALLIDO)
    @Test
    void testIniciarSesionError() {
        // Preparación
        sesionServicio.logIn(1L);

        // Testeo
        boolean resultado = sesionServicio.logIn(1L);

        // Validación
        assertFalse(resultado);
    }


    // TEST VALIDAR USUARIO LOGEADO (EXITOSO)
    @Test
    void testIsLoggedIn() {
        // Preparación
        Long usuario = 1L;

        sesionServicio.logIn(usuario);

        // Testeo
        boolean resultado = sesionServicio.isLoggedIn(usuario);

        // Validación
        assertTrue(resultado);
    }


    // TEST VALIDAR USUARIO LOGEADO (FALLIDO)
    @Test
    void testIsLoggedInError() {
        // Preparación
        Long usuario = 1L;

        // Testeo
        boolean resultado = sesionServicio.isLoggedIn(usuario);

        // Validación
        assertFalse(resultado);
    }


    // TEST VALIDAR CERRAR SESION (EXITOSO)
    @Test
    void testCerrarSesion() {
        // Preparación
        Long usuario = 1L;

        sesionServicio.logIn(usuario);

        // Testeo
        boolean resultado = sesionServicio.logOut(usuario);

        // Validación
        assertTrue(resultado);
    }


    // TEST VALIDAR CERRAR SESION (FALLIDO)
    @Test
    void testCerrarSesionError() {
        // Preparación
        Long usuario = 1L;

        // Testeo
        boolean resultado = sesionServicio.logOut(usuario);

        // Validación
        assertFalse(resultado);
    }


    // TEST VALIDAR USUARIOS (EXITOSO)
    @Test
    void testValidarUsuario() {
        // Preparación
        Long usuario = 1L;

        sesionServicio.logIn(usuario);

        // Configuración
        when(usuarioRepo.existsById(usuario)).thenReturn(true);

        // Testeo
        boolean resultado = sesionServicio.validacionUsuario(usuario);

        // Validación
        assertTrue(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).existsById(usuario);
    }


    // TEST VALIDAR USUARIOS (FALLIDO: USUARIO NO EXISTE)
    @Test
    void testValidarUsuarioNotFound() {
        // Preparación
        Long usuario = 1L;

        sesionServicio.logIn(usuario);

        // Configuración
        when(usuarioRepo.existsById(usuario)).thenReturn(false);

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            sesionServicio.validacionUsuario(usuario);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(usuario);
    }


    // TEST VALIDAR USUARIOS (FALLIDO: USUARIO NO LOGEADO)
    @Test
    void testValidarUsuarioNotLoggedIn() {
        // Preparación
        Long usuario = 1L;

        // Configuración
        when(usuarioRepo.existsById(usuario)).thenReturn(true);

        // Testeo
        assertThrows(NotLoggedInException.class, () -> {
            sesionServicio.validacionUsuario(usuario);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(usuario);
    }


    // TEST VALIDAR EMPLEADOS (EXITOSO)
    @Test
    void testValidarEmpleado() {
        // Preparación
        Long id = 1L;
        int filtro = 2;

        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setNvlPermiso(3);

        sesionServicio.logIn(id);

        // Configuración
        when(usuarioRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.findById(id)).thenReturn(Optional.of(empleado));

        // Testeo
        boolean resultado = sesionServicio.validacionEmpleado(id, filtro);

        // Validación
        assertTrue(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).findById(id);

    }


    // TEST VALIDAR EMPLEADOS (FALLIDO: USUARIO NO REGISTRADO)
    @Test
    void testValidarEmpleadoNoRegistrado() {
        // Preparación
        Long id = 1L;
        int filtro = 2;

        // Configuración
        when(usuarioRepo.existsById(id)).thenReturn(false);

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            sesionServicio.validacionEmpleado(id, filtro);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(id);
    }


    // TEST VALIDAR EMPLEADOS (FALLIDO: USUARIO NO ES EMPLEADO)
    @Test
    void testValidarEmpleadoNotEmployee() {
        // Preparación
        Long id = 1L;
        int filtro = 2;

        // Configuración
        when(usuarioRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.existsById(id)).thenReturn(false);

        // Testeo y Validación
        assertThrows(EmployeesOnlyException.class, () -> {
            sesionServicio.validacionEmpleado(id, filtro);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).existsById(id);

    }


    // TEST VALIDAR EMPLEADOS (FALLIDO: EMPLEADO NO LOGEADO)
    @Test
    void testValidarEmpleadoNotLoggedIn() {
        // Preparación
        Long id = 1L;
        int filtro = 2;

        // Configuración
        when(usuarioRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.existsById(id)).thenReturn(true);

        // Testeo y Validación
        assertThrows(NotLoggedInException.class, () -> {
            sesionServicio.validacionEmpleado(id, filtro);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).existsById(id);
    }


    // TEST VALIDAR EMPLEADOS (FALLIDO: SIN PERMISOS SUFICIENTES)
    @Test
    void testValidarEmpleadoSinPermisos() {
        // Preparación
        Long id = 1L;
        int filtro = 4;

        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setNvlPermiso(3);

        sesionServicio.logIn(id);

        // Configuración
        when(usuarioRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.existsById(id)).thenReturn(true);
        when(empleadoRepo.findById(id)).thenReturn(Optional.of(empleado));

        // Testeo y Validación
        assertThrows(SinPermisosException.class, () -> {
            sesionServicio.validacionEmpleado(id, filtro);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).existsById(id);
        verify(empleadoRepo, times(1)).findById(id);
    }


    // TEST VALIDAR CLIENTES (EXITOSO)
    @Test
    void testValidarCliente() {
        // Preparación
        Long cliente = 1L;

        sesionServicio.logIn(cliente);

        // Configuración
        when(usuarioRepo.existsById(cliente)).thenReturn(true);
        when(clienteRepo.existsById(cliente)).thenReturn(true);

        // Testeo
        boolean resultado = sesionServicio.validacionCliente(cliente);

        // Validación
        assertTrue(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).existsById(cliente);
        verify(clienteRepo, times(1)).existsById(cliente);
    }


    // TEST VALIDAR CLIENTES (FALLIDO: USUARIO NO REGISTRADO)
    @Test
    void testValidarClienteNoRegistrado() {
        // Preparación
        Long cliente = 1L;

        // Configuración
        when(usuarioRepo.existsById(cliente)).thenReturn(false);

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            sesionServicio.validacionCliente(cliente);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(cliente);
    }


    // TEST VALIDAR CLIENTES (FALLIDO: USUARIO NO ES UN CLIENTE)
    @Test
    void testValidarClienteNotClient() {
        // Preparación
        Long cliente = 1L;

        // Configuración
        when(usuarioRepo.existsById(cliente)).thenReturn(true);
        when(clienteRepo.existsById(cliente)).thenReturn(false);

        // Testeo y Validación
        assertThrows(ClientesOnlyException.class, () -> {
            sesionServicio.validacionCliente(cliente);
        });

        // Verificación
        verify(usuarioRepo, times(1)).existsById(cliente);
        verify(clienteRepo, times(1)).existsById(cliente);
    }
}

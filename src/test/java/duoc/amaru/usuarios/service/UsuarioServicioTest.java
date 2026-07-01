package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.error.exceptions.DisabledUserException;
import duoc.amaru.usuarios.error.exceptions.EmailNotFoundException;
import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.error.exceptions.WrongPasswordException;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class UsuarioServicioTest {
    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private SesionServicio sesionServicio;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    @BeforeEach
    void sutup() {
        MockitoAnnotations.openMocks(this);
    }


    // TEST INICIAR SESION DE USUARIO (EXITOSO)
    @Test
    void testIniciarSesion() {
        // Preparación
        String correo = "am.bu@gmail.com";
        String password = "testeo";
        Long id = 7L;

        Cliente user = new Cliente();
        user.setId(id);
        user.setCorreo(correo);
        user.setPassword(password);
        user.setEstado("Activo");

        // Configuración
        when(usuarioRepo.findByCorreo(correo)).thenReturn(user);
        when(sesionServicio.isLoggedIn(id)).thenReturn(false);

        // Testeo
        Usuario resultado = usuarioServicio.iniciarSesion(correo, password);

        // Validación
        assertNotNull(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).findByCorreo(correo);
        verify(sesionServicio, times(1)).isLoggedIn(id);
    }


    // TEST INICIAR SESION DE USUARIO (FALLIDO: CORREO NO ENCONTRADO)
    @Test
    void testIniciarSesionErrorEmail() {
        // Preparación
        String correo = "am.bu@gmail.com";
        String password = "testeo";

        // Configuración
        when(usuarioRepo.findByCorreo(correo)).thenReturn(null);

        // Testeo y Validación
        assertThrows(EmailNotFoundException.class, () -> {
            usuarioServicio.iniciarSesion(correo, password);
        });

        // Verificación
        verify(usuarioRepo, times(1)).findByCorreo(correo);
    }


    // TEST INICIAR SESION DE USUARIO (FALLIDO: CONTRASEÑA INCORRECTA)
    @Test
    void testIniciarSesionErrorPassword() {
        // Preparación
        String correo = "am.bu@gmail.com";
        String password = "contraseña_incorrecta";

        Cliente user = new Cliente();
        user.setPassword("testeo");

        // Configuración
        when(usuarioRepo.findByCorreo(correo)).thenReturn(user);

        // Testeo y Validación
        assertThrows(WrongPasswordException.class, () -> {
            usuarioServicio.iniciarSesion(correo, password);
        });

        // Verificación
        verify(usuarioRepo, times(1)).findByCorreo(correo);
    }


    // TEST INICIAR SESION DE USUARIO (FALLIDO: CUENTA DESACTIVADA)
    @Test
    void testIniciarSesionErrorEstado() {
        // Preparación
        String correo = "am.bu@gmail.com";
        String password = "testeo";

        Cliente user = new Cliente();
        user.setCorreo(correo);
        user.setPassword(password);
        user.setEstado("Desactivado");

        // Configuración
        when(usuarioRepo.findByCorreo(correo)).thenReturn(user);

        // Testeo y Validación
        assertThrows(DisabledUserException.class, () -> {
            usuarioServicio.iniciarSesion(correo, password);
        });

        // Verificación
        verify(usuarioRepo, times(1)).findByCorreo(correo);
    }


    // TEST INICIAR SESION DE USUARIO (FALLIDO: USUARIO YA LOGEADO)
    @Test
    void testIniciarSesionYaLogeado() {
        // Preparación
        String correo = "am.bu@gmail.com";
        String password = "testeo";
        Long id = 7L;

        Cliente user = new Cliente();
        user.setId(id);
        user.setPassword(password);
        user.setEstado("Activo");

        // Configuración
        when(usuarioRepo.findByCorreo(correo)).thenReturn(user);
        when(sesionServicio.isLoggedIn(id)).thenReturn(true);

        // Testeo
        Usuario resultado = usuarioServicio.iniciarSesion(correo, password);

        // Validación
        assertNull(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).findByCorreo(correo);
        verify(sesionServicio, times(1)).isLoggedIn(id);
    }


    // TEST CERRAR SESION DE USUARIO (EXITOSO)
    @Test
    void testCerrarSesion() {
        // Preparación
        Long id = 7L;
        Empleado user = new Empleado();

        // Configuración
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(user));
        when(sesionServicio.isLoggedIn(id)).thenReturn(true);

        // Testeo
        Usuario resultado = usuarioServicio.cerrarSesion(id);

        // Validación
        assertNotNull(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).findById(id);
        verify(sesionServicio, times(1)).isLoggedIn(id);
    }


    // TEST CERRAR SESION DE USUARIO (FALLIDO: USUARIO NO ENCONTRADO)
    @Test
    void testCerrarSesionNotFound() {
        // Preparación
        Long id = 7L;

        // Configuración
        when(usuarioRepo.findById(id)).thenReturn(Optional.empty());

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            usuarioServicio.cerrarSesion(id);
        });

        // Verificación
        verify(usuarioRepo, times(1)).findById(id);
    }


    // TEST CERRAR SESION DE USUARIO (FALLIDO: SESION YA CERRADA)
    @Test
    void testCerrarSesionNoLogeado() {
        // Preparación
        Long id = 7L;
        Empleado user = new Empleado();

        // Configuración
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(user));
        when(sesionServicio.isLoggedIn(id)).thenReturn(false);

        // Testeo
        Usuario resultado = usuarioServicio.cerrarSesion(id);

        // Validación
        assertNull(resultado);

        // Verificación
        verify(usuarioRepo, times(1)).findById(id);
        verify(sesionServicio, times(1)).isLoggedIn(id);
    }


    // TODO: Descrubrir si este metodo es utilizado y para qué
    @Test
    void testReadAllUsers() {
        
    }
}

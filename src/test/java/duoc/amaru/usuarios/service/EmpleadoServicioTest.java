package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.EmpleadoRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class EmpleadoServicioTest {
    @Mock
    private EmpleadoRepo empleadoRepo;

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private SesionServicio sesionServicio;

    @InjectMocks
    private EmpleadoServicio empleadoServicio;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    
    // TEST REGISTRAR EMPLEADO (EXITOSO: GERENTE)
    @Test
    void testRegistrarEmpleadoGerente() {
        // Preparación
        Long admin = 8L;
        Empleado entrante = new Empleado(null, "21.210.987-6",
                                         "Alexis", null,
                                         "Oñate", null,
                                         "al.onn@gmail.com", "testeo",
                                         "+56 9 8877 6655", null,
                                         0, "gErEnTe", LocalDate.now());                                        

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 3)).thenReturn(true);
        when(usuarioRepo.existsByCorreo("al.onn@gmail.com")).thenReturn(false);
        when(usuarioRepo.save(any(Empleado.class)))
        .thenAnswer(e -> {
            Empleado input = e.getArgument(0);
            input.setId(1L);
            return input;
        });

        // Testeo
        Empleado resultado = empleadoServicio.registrarUsuario(admin, entrante);

        // Validación
        assertNotNull(resultado);
        assertEquals("gerente", resultado.getCargo());
        assertEquals("Activo", resultado.getEstado());
        assertEquals(3, resultado.getNvlPermiso());
        
        // Verificar
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 3);
        verify(usuarioRepo, times(1)).existsByCorreo("al.onn@gmail.com");
        verify(usuarioRepo, times(1)).save(entrante);
    }


    // TEST REGISTRAR EMPLEADO (EXITOSO: ENCARGADO DE VENTAS)
    @Test
    void testRegistrarEmpleadoVentas() {
        // Preparación
        Long admin = 8L;
        Empleado entrante = new Empleado(null, "21.210.987-6",
                                         "Alexis", null,
                                         "Oñate", null,
                                         "al.onn@gmail.com", "testeo",
                                         "+56 9 8877 6655", null,
                                         0, "ENCARGADO DE VENTAS", LocalDate.now());

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 3)).thenReturn(true);
        when(usuarioRepo.existsByCorreo("al.onn@gmail.com")).thenReturn(false);
        when(usuarioRepo.save(any(Empleado.class)))
        .thenAnswer(e -> {
            Empleado input = e.getArgument(0);
            input.setId(1L);
            return input;
        });

        // Testeo
        Empleado resultado = empleadoServicio.registrarUsuario(admin, entrante);

        // Validación
        assertNotNull(resultado);
        assertEquals("encargado de ventas", resultado.getCargo());
        assertEquals("Activo", resultado.getEstado());
        assertEquals(2, resultado.getNvlPermiso());
        
        // Verificar
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 3);
        verify(usuarioRepo, times(1)).existsByCorreo("al.onn@gmail.com");
        verify(usuarioRepo, times(1)).save(entrante);
    }


    // TEST REGISTRAR EMPLEADO (EXITOSO: ADMIN)
    @Test
    void testRegistrarEmpleadoAdmin() {
        // Preparación
        Long admin = 8L;
        Empleado entrante = new Empleado(null, "21.210.987-6",
                                         "Alexis", null,
                                         "Oñate", null,
                                         "al.onn@gmail.com", "testeo",
                                         "+56 9 8877 6655", null,
                                         8, "aDMIn", LocalDate.now());

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 3)).thenReturn(true);
        when(usuarioRepo.existsByCorreo("al.onn@gmail.com")).thenReturn(false);
        when(usuarioRepo.save(any(Empleado.class)))
        .thenAnswer(e -> {
            Empleado input = e.getArgument(0);
            input.setId(1L);
            return input;
        });

        // Testeo
        Empleado resultado = empleadoServicio.registrarUsuario(admin, entrante);

        // Validación
        assertNotNull(resultado);
        assertEquals("admin", resultado.getCargo());
        assertEquals("Activo", resultado.getEstado());
        assertEquals(4, resultado.getNvlPermiso());
        
        // Verificar
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 3);
        verify(usuarioRepo, times(1)).existsByCorreo("al.onn@gmail.com");
        verify(usuarioRepo, times(1)).save(entrante);
    }


    // TEST REGISTRAR EMPLEADO (FALLIDO)
    @Test
    void testRegistrarEmpleadoError() {
        // Preparación
        Long gerente = 8L;
        Empleado entrante = new Empleado();
        entrante.setCorreo("al.onn@gmail.com");
        
        // Configuración
        when(sesionServicio.validacionEmpleado(gerente, 3)).thenReturn(true);
        when(usuarioRepo.existsByCorreo("al.onn@gmail.com")).thenReturn(true);

        // Testeo
        Empleado resultado = empleadoServicio.registrarUsuario(gerente, entrante);

        // Validación
        assertNull(resultado);
        
        // Verificar
        verify(sesionServicio, times(1)).validacionEmpleado(gerente, 3);
        verify(usuarioRepo, times(1)).existsByCorreo("al.onn@gmail.com");
        verify(usuarioRepo, times(0)).save(entrante);
    }


    // TEST ACTUALIZAR PERMISOS DE USUARIO (EXITOSO)
    @Test
    void testUpdatePermisos() {
        // Preparación
        Long admin = 4L;
        Long id = 1L;
        int lvl = 2;

        Empleado user = new Empleado();
    
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(user));
        when(usuarioRepo.save(user)).thenReturn(user);

        // Testeo
        Usuario resultado = empleadoServicio.updateUsuario(id, lvl, admin);

        // Validación
        assertNotNull(resultado);
        assertEquals(2, resultado.getNvlPermiso());

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(id);
        verify(usuarioRepo, times(1)).save(user);
    }


    // TEST ACTUALIZAR PERMISOS DE USUARIO (FALLIDO: PERMISO FUERA DE RANGO)
    @Test
    void testUpdatePermisosError() {
        // Preparación
        Long admin = 4L;
        Long id = 1L;
        int lvl = 5;

        Empleado user = new Empleado();
    
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(user));

        // Testeo
        Usuario resultado = empleadoServicio.updateUsuario(id, lvl, admin);

        // Validación
        assertNull(resultado);

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(id);
        verify(usuarioRepo, times(0)).save(user);
    }


    // TEST ACTUALIZAR PERMISOS DE USUARIO (FALLIDO: USUARIO NO ENCONTRADO)
    @Test
    void testUpdatePermisosNotFound() {
        // Preparación
        Long admin = 4L;
        Long id = 1L;
        int lvl = 2;

        Empleado user = new Empleado();
    
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(id)).thenReturn(Optional.empty());

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            empleadoServicio.updateUsuario(id, lvl, admin);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(id);
        verify(usuarioRepo, times(0)).save(user);
    }


    // TEST ACTUALIZAR CARGO EMPLEADO (EXITOSO)
    @Test
    void testUpdateCargo() {
        // Preparación
        Long admin = 8L;
        Long empleado = 6L;
        String cargo = "encargado de ventas";

        Empleado user = new Empleado();
        
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(empleadoRepo.findById(empleado)).thenReturn(Optional.of(user));
        when(empleadoRepo.save(user)).thenReturn(user);

        // Testeo
        Empleado resultado = empleadoServicio.updateUsuario(empleado, cargo, admin);

        // Validación
        assertNotNull(resultado);
        assertEquals("encargado de ventas", resultado.getCargo());
        
        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(empleadoRepo, times(1)).findById(empleado);
        verify(empleadoRepo, times(1)).save(user);
    }


    // TEST ACTUALIZAR CARGO EMPLEADO (FALLIDO: AUTENTICACIÓN)
    @Test
    void testUpdateCargoError() {
        // Preparación
        Long admin = 8L;
        Long empleado = 6L;
        String cargo = "encargado de ventas";

        Empleado user = new Empleado();
        
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(empleadoRepo.findById(empleado)).thenReturn(Optional.empty());

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            empleadoServicio.updateUsuario(empleado, cargo, admin);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(empleadoRepo, times(1)).findById(empleado);
        verify(usuarioRepo, times(0)).save(user);
    }


    // TEST ACTUALIZAR CARGO EMPLEADO (FALLIDO: CARGO INVALIDO)
    @Test
    void testUpdateCargoNotFound() {
        // Preparación
        Long admin = 8L;
        Long empleado = 6L;
        String cargo = "cientista de datos";

        Empleado user = new Empleado();
        
        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(empleadoRepo.findById(empleado)).thenReturn(Optional.of(user));

        // Testeo
        Empleado resultado = empleadoServicio.updateUsuario(empleado, cargo, admin);

        // Validación
        assertNull(resultado);
        
        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(empleadoRepo, times(1)).findById(empleado);
        verify(usuarioRepo, times(0)).save(user);
    }


    // TEST DESACTIVAR USUARIO (EXITOSO)
    @Test
    void testDesactivarUsuario() {
        // Preparación
        Long admin = 8L;
        Long usuario = 1L;

        Usuario user = new Cliente();

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(usuario)).thenReturn(Optional.of(user));
        when(usuarioRepo.save(user)).thenReturn(user);

        // Testeo
        Usuario resultado = empleadoServicio.desactivarUser(usuario, admin);

        // Validación
        assertNotNull(resultado);
        assertEquals("desactivado", resultado.getEstado());

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(usuario);
        verify(usuarioRepo, times(1)).save(user);
    }


    // TEST DESACTIVAR USUARIO (FALLIDO)
    @Test
    void testDesactivarUsuarioError() {
        // Preparación
        Long admin = 8L;
        Long usuario = 1L;

        Usuario user = new Cliente();

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(usuario)).thenReturn(Optional.empty());

        // Testeo y Validación
        assertThrows(NotSignedInException.class, () -> {
            empleadoServicio.desactivarUser(usuario, admin);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(usuario);
        verify(usuarioRepo, times(0)).save(user);
    }


    // TEST ELIMINAR USUARIO (EXITOSO)
    @Test
    void testEliminarUsuario() {
        // Preparación
        Long admin = 8L;
        Long usuario = 1L;

        Usuario user = new Empleado();

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(usuario)).thenReturn(Optional.of(user));

        // Testeo
        boolean resultado = empleadoServicio.eliminarUser(usuario, admin);

        // Validación
        assertTrue(resultado);

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(usuario);
    }


    // TEST ELIMINAR USUARIO (FALLIDO)
    @Test
    void testEliminarUsuarioError() {
        // Preparación
        Long admin = 8L;
        Long usuario = 1L;

        // Configuración
        when(sesionServicio.validacionEmpleado(admin, 4)).thenReturn(true);
        when(usuarioRepo.findById(usuario)).thenReturn(Optional.empty());

        // Testeo
        boolean resultado = empleadoServicio.eliminarUser(usuario, admin);

        // Validación
        assertFalse(resultado);

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(admin, 4);
        verify(usuarioRepo, times(1)).findById(usuario);
    }
}

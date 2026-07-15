package duoc.amaru.usuarios.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import duoc.amaru.usuarios.dto.GetClienteDTO;
import duoc.amaru.usuarios.dto.GetDireccionDTO;
import duoc.amaru.usuarios.dto.LogInDTO;
import duoc.amaru.usuarios.dto.UpdateDirDTO;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.service.ClienteServicio;
import duoc.amaru.usuarios.service.EmpleadoServicio;
import duoc.amaru.usuarios.service.UsuarioServicio;

public class UsuarioControladorTest {

    @Mock
    private UsuarioServicio usuarioServicio;

    @Mock
    private ClienteServicio clienteServicio;

    @Mock
    private EmpleadoServicio empleadoServicio;

    @InjectMocks
    private UsuarioControlador usuarioControlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postRegistrarCliente_Exito() {
        Cliente nuevoCliente = new Cliente();
        when(clienteServicio.registrarUsuario(any(Cliente.class))).thenReturn(nuevoCliente);

        ResponseEntity<?> response = usuarioControlador.postRegistrarCliente(nuevoCliente);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Usuario registrado exitosamente", response.getBody());
    }

    @Test
    void postRegistrarCliente_Fallo() {
        Cliente nuevoCliente = new Cliente();
        when(clienteServicio.registrarUsuario(any(Cliente.class))).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.postRegistrarCliente(nuevoCliente);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("El correo ingresado ya está registrado", response.getBody());
    }

    @Test
    void postRegistrarEmpleado_Exito() {
        Empleado nuevoEmpleado = new Empleado();
        when(empleadoServicio.registrarUsuario(eq(1L), any(Empleado.class))).thenReturn(nuevoEmpleado);

        ResponseEntity<?> response = usuarioControlador.postRegistrarEmpleado(nuevoEmpleado, 1L);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Usuario registrado exitosamente", response.getBody());
    }

    @Test
    void postRegistrarEmpleado_Fallo() {
        Empleado nuevoEmpleado = new Empleado();
        when(empleadoServicio.registrarUsuario(eq(1L), any(Empleado.class))).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.postRegistrarEmpleado(nuevoEmpleado, 1L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("El correo ingresado ya está registrado", response.getBody());
    }

    @Test
    void postInicioSesion_Exito() {
        LogInDTO dto = new LogInDTO();
        dto.setCorreo("test@test.com");
        dto.setPassword("1234");

        Cliente cli = new Cliente();
        cli.setPNombre("Juan");
        cli.setId(5L);

        when(usuarioServicio.iniciarSesion("test@test.com", "1234")).thenReturn(cli);

        ResponseEntity<?> response = usuarioControlador.postInicioSesion(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Iniciaste sesión como <Juan> con Id: #5", response.getBody());
    }

    @Test
    void postInicioSesion_Fallo() {
        LogInDTO dto = new LogInDTO();
        dto.setCorreo("test@test.com");
        dto.setPassword("1234");

        when(usuarioServicio.iniciarSesion("test@test.com", "1234")).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.postInicioSesion(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Su sesión ya está iniciada", response.getBody());
    }

    @Test
    void getCerrarSesion_Exito() {
        Cliente cli = new Cliente();
        cli.setPNombre("Juan");

        when(usuarioServicio.cerrarSesion(1L)).thenReturn(cli);

        ResponseEntity<?> response = usuarioControlador.getCerrarSesion(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se cerró la sesión de Juan", response.getBody());
    }

    @Test
    void getCerrarSesion_Fallo() {
        when(usuarioServicio.cerrarSesion(1L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.getCerrarSesion(1L);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Usuario no autenticado. La sesión ya está cerrada", response.getBody());
    }

    @Test
    void putConfigUsuario_Exito() {
        Cliente cli = new Cliente();
        cli.setPNombre("Juan");
        cli.setAPaterno("Perez");

        when(empleadoServicio.updateUsuario(1L, 2, 3L)).thenReturn(cli);

        ResponseEntity<?> response = usuarioControlador.putConfigUsuario(1L, 2, 3L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Permisos actualizados para usuario Juan Perez", response.getBody());
    }

    @Test
    void putConfigUsuario_Fallo() {
        when(empleadoServicio.updateUsuario(1L, 2, 3L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.putConfigUsuario(1L, 2, 3L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Nuevo nivel de acceso fuera de rango", response.getBody());
    }

    @Test
    void putCargoEmpleado_Exito() {
        Empleado emp = new Empleado();
        emp.setPNombre("Juan");
        emp.setAPaterno("Perez");

        when(empleadoServicio.updateUsuario(1L, "Admin", 2L)).thenReturn(emp);

        ResponseEntity<?> response = usuarioControlador.putCargoEmpleado(1L, "Admin", 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Cargo actualizado para empleado Juan Perez", response.getBody());
    }

    @Test
    void putCargoEmpleado_Fallo() {
        when(empleadoServicio.updateUsuario(1L, "Admin", 2L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.putCargoEmpleado(1L, "Admin", 2L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Cargo inválido", response.getBody());
    }

    @Test
    void postDireccionEnvio_Exito() {
        Direccion dir = new Direccion();
        dir.setEtiqueta("Casa");

        GetClienteDTO dto = new GetClienteDTO();
        dto.setNombre("Juan");

        when(clienteServicio.addDireccion(eq(1L), any(Direccion.class))).thenReturn(dir);
        when(clienteServicio.getClienteById(0L, 1L)).thenReturn(dto);

        ResponseEntity<?> response = usuarioControlador.postDireccionEnvio(dir, 1L);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Dirección añadida correctamente a Juan como \"Casa\"", response.getBody());
    }

    @Test
    void getDireccionesEnvio_Vacio() {
        when(clienteServicio.getDirecciones(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = usuarioControlador.getDireccionesEnvio(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("No hay direcciones de envío guardadas", response.getBody());
    }

    @Test
    void getDireccionesEnvio_ConUnDato() {
        GetDireccionDTO dirDto = new GetDireccionDTO();
        List<GetDireccionDTO> lista = List.of(dirDto);

        when(clienteServicio.getDirecciones(1L)).thenReturn(lista);

        ResponseEntity<?> response = usuarioControlador.getDireccionesEnvio(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("1 dirección de envío guardada:\n" + lista.toString(), response.getBody());
    }

    @Test
    void getDireccionesEnvio_ConDatos() {
        GetDireccionDTO dirDto = new GetDireccionDTO();
        GetDireccionDTO dirDto2 = new GetDireccionDTO();
        List<GetDireccionDTO> lista = List.of(dirDto, dirDto2);

        when(clienteServicio.getDirecciones(1L)).thenReturn(lista);

        ResponseEntity<?> response = usuarioControlador.getDireccionesEnvio(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("2 direcciones de envío guardadas:\n" + lista.toString(), response.getBody());
    }

    @Test
    void getDireccionEnvio_Exito() {
        Direccion dir = new Direccion();
        when(clienteServicio.getDireccion(1L, 2L)).thenReturn(dir);

        ResponseEntity<?> response = usuarioControlador.getDireccionEnvio(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dir, response.getBody());
    }

    @Test
    void getDireccionEnvio_Fallo() {
        when(clienteServicio.getDireccion(1L, 2L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.getDireccionEnvio(1L, 2L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Dirección no encontrada", response.getBody());
    }

    @Test
    void putDireccionEnvio_Exito() {
        UpdateDirDTO dto = new UpdateDirDTO();
        when(clienteServicio.updateDireccion(1L, dto, 2L)).thenReturn(new Direccion());

        ResponseEntity<?> response = usuarioControlador.putDireccionEnvio(dto, 1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dirección actualizada correctamente", response.getBody());
    }

    @Test
    void putDireccionEnvio_Fallo() {
        UpdateDirDTO dto = new UpdateDirDTO();
        when(clienteServicio.updateDireccion(1L, dto, 2L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.putDireccionEnvio(dto, 1L, 2L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Dirección no encontrada", response.getBody());
    }

    @Test
    void deleteDireccionEnvio_Exito() {
        when(clienteServicio.deleteDireccion(1L, 2L)).thenReturn(new Cliente());

        ResponseEntity<?> response = usuarioControlador.deleteDireccionEnvio(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Dirección eliminada correctamente", response.getBody());
    }

    @Test
    void deleteDireccionEnvio_Fallo() {
        when(clienteServicio.deleteDireccion(1L, 2L)).thenReturn(null);

        ResponseEntity<?> response = usuarioControlador.deleteDireccionEnvio(1L, 2L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Dirección no encontrada", response.getBody());
    }

    @Test
    void putDesactivarUser_Exito() {
        Cliente cli = new Cliente();
        cli.setPNombre("Juan");
        cli.setAPaterno("Perez");

        when(empleadoServicio.desactivarUser(1L, 2L)).thenReturn(cli);

        ResponseEntity<?> response = usuarioControlador.putDesactivarUser(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("La cuenta de Juan Perez ha sido desactivada", response.getBody());
    }

    @Test
    void deleteUsuario_Exito() {
        when(empleadoServicio.eliminarUser(1L, 2L)).thenReturn(true);

        ResponseEntity<?> response = usuarioControlador.deleteUsuario(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Usuario con Id #1 eliminado", response.getBody());
    }

    @Test
    void deleteUsuario_Fallo() {
        when(empleadoServicio.eliminarUser(1L, 2L)).thenReturn(false);

        ResponseEntity<?> response = usuarioControlador.deleteUsuario(1L, 2L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No se hayó un usuario con Id #1", response.getBody());
    }
}
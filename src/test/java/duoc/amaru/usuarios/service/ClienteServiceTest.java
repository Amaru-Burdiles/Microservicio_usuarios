package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;

import duoc.amaru.usuarios.dto.GetClienteDTO;
import duoc.amaru.usuarios.dto.GetClientesDTO;
import duoc.amaru.usuarios.dto.GetDireccionDTO;
import duoc.amaru.usuarios.dto.UpdateDirDTO;
import duoc.amaru.usuarios.enums.Comuna;
import duoc.amaru.usuarios.enums.Region;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.DireccionRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class ClienteServiceTest {
    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private ClienteRepo clienteRepo;

    @Mock
    private DireccionRepo direccionRepo;

    @Mock
    private SesionServicio sesionServicio;

    @InjectMocks
    private ClienteServicio clienteServicio;

    @BeforeEach
    void setUp() {
        // Inicializar mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    // TEST REGISTRAR CLIENTE (EXITOSO)
    @Test
    void testRegistrarCliente() {
        // Preparación de Objetos
        Cliente clienteNew = new Cliente(null, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", null, 0, null);
        Cliente clienteSaved = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        // Definición lógica Mockito
        when(usuarioRepo.save(clienteNew)).thenReturn(clienteSaved);
        when(usuarioRepo.existsByCorreo("a.b@duoc.cl")).thenReturn(false);

        // Ejecutar servicio real
        Cliente resultado = clienteServicio.registrarUsuario(clienteNew);

        // El cliente se guardo?
        assertNotNull(resultado);

        // El cliente guardado por el Servicio es igual al esperado?
        assertEquals(clienteSaved, resultado);

        // Los valores rellenados automaticamante son los esperados?
        assertEquals(1L, resultado.getId());
        assertEquals(1, resultado.getNvlPermiso());
        assertEquals("activo", resultado.getEstado());

        // Cuantas veces se llamo a .save()?
        verify(usuarioRepo, times(1)).save(clienteNew);
    }


    // TEST REGISTRAR CLIENTE (FALLIDO)
    @Test
    void testSignInClienteDuplicado() {
        // Preparación de Objetos
        Cliente clienteNew = new Cliente(null, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", null, 0, null);
        Cliente clienteSaved = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        // Definición lógica Mockito
        when(usuarioRepo.save(clienteNew)).thenReturn(clienteSaved);
        when(usuarioRepo.existsByCorreo("a.b@duoc.cl")).thenReturn(true);

        // Ejecución servicio real
        Cliente resultado = clienteServicio.registrarUsuario(clienteNew);

        // Cliente no registrado
        assertNull(resultado);

        // Ejecuciones de .save()
        verify(usuarioRepo, times(0)).save(clienteNew);
    }


    // TEST OBTENER CLIENTES (EXITOSO: TODOS)
    @Test
    void testMostrarClientesLleno() {
        // Preparación
        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c2 = new Cliente(2L, "23.234.234-5", "Rocio", null, "Bustos", null, "r.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c3 = new Cliente(3L, "34.345.345-6", "Dairys", null, "Bernal", null, "d.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        List<Cliente> fullCli = new ArrayList<>();
        fullCli.add(c1);
        fullCli.add(c2);
        fullCli.add(c3);

        Long idEjecutor = 5L;

        // Configuración
        when(sesionServicio.validacionEmpleado(idEjecutor, 2)).thenReturn(true);
        when(clienteRepo.findAll()).thenReturn(fullCli);

        // Testeo
        List<GetClientesDTO> resultado = clienteServicio.getAllClientes(idEjecutor);

        // Validación
        assertEquals(3, resultado.size());
        assertEquals("Amaru Burdiles", resultado.getFirst().getNombreApellido());

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(idEjecutor, 2);
        verify(clienteRepo, times(1)).findAll();
    }


    // TEST OBTENER CLIENTES (EXITOSO: VACIO)
    @Test
    void testMostrarClientesVacio() {
        // Preparación
        List<Cliente> fullCli = new ArrayList<>();

        Long idEjecutor = 5L;

        // Configuración
        when(sesionServicio.validacionEmpleado(idEjecutor, 2)).thenReturn(true);
        when(clienteRepo.findAll()).thenReturn(fullCli);

        // Testeo
        List<GetClientesDTO> resultado = clienteServicio.getAllClientes(idEjecutor);

        // Validación
        assertEquals(0, resultado.size());

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(idEjecutor, 2);
        verify(clienteRepo, times(1)).findAll();
    }


    // TEST OBTENER CLIENTES (EXITOSO: LLAMADA DEL SISTEMA)
    @Test
    void testMostrarClientesSysCall() {
        // Preparación
        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c2 = new Cliente(2L, "23.234.234-5", "Rocio", null, "Bustos", null, "r.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c3 = new Cliente(3L, "34.345.345-6", "Dairys", null, "Bernal", null, "d.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        List<Cliente> fullCli = new ArrayList<>();
        fullCli.add(c1);
        fullCli.add(c2);
        fullCli.add(c3);

        Long idEjecutor = 0L;

        // Configuración
        when(sesionServicio.validacionEmpleado(idEjecutor, 2)).thenReturn(true);
        when(clienteRepo.findAll()).thenReturn(fullCli);

        // Testeo
        List<GetClientesDTO> resultado = clienteServicio.getAllClientes(idEjecutor);

        // Validación
        assertEquals(3, resultado.size());
        assertEquals("Amaru Burdiles", resultado.getFirst().getNombreApellido());

        // Verificación
        verify(sesionServicio, times(0)).validacionEmpleado(idEjecutor, 2);
        verify(clienteRepo, times(1)).findAll();
    }


    // TEST OBTENER CLIENTES (FALLIDO: PERMISO INSUFICIENTE)
    @Test
    void testMostrarClientesError() {
        // Preparación
        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c2 = new Cliente(2L, "23.234.234-5", "Rocio", null, "Bustos", null, "r.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        Cliente c3 = new Cliente(3L, "34.345.345-6", "Dairys", null, "Bernal", null, "d.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        List<Cliente> fullCli = new ArrayList<>();
        fullCli.add(c1);
        fullCli.add(c2);
        fullCli.add(c3);

        Long idEjecutor = 1L;

        // Configuración
        when(sesionServicio.validacionEmpleado(idEjecutor, 2)).thenThrow(HttpClientErrorException.class);
        when(clienteRepo.findAll()).thenReturn(fullCli);

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            clienteServicio.getAllClientes(idEjecutor);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionEmpleado(idEjecutor, 2);
        verify(clienteRepo, times(0)).findAll();
    }


    // TEST OBTENER CLIENTE POR ID (EXITOSO: DUEÑO DE LA INFO)
    @Test
    void testGetClienteByIdOwner() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion(2L, "Casa mamá", "Paicavi", 810, 69, null, Comuna.CONCEPCION, Region.BIOBIO);

        List<Direccion> dirs = new ArrayList<>();
        dirs.add(d1);
        dirs.add(d2);

        Long id = 1L;

        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", "Antú", "Burdiles", "Jarpa", "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, dirs);
        
        // Configuración
        when(sesionServicio.validacionUsuario(id)).thenReturn(true);
        when(clienteRepo.findById(id)).thenReturn(Optional.of(c1));
        when(sesionServicio.validacionEmpleado(id, 4)).thenReturn(false);

        // Testeo
        GetClienteDTO resultado = clienteServicio.getClienteById(id, id);

        // Validación
        assertNotNull(resultado);
        
        assertEquals(c1.getId(), resultado.getIdCliente());
        assertEquals(c1.getCorreo(), resultado.getCorreo());
        assertEquals(c1.getTelefono(), resultado.getTelefono());
        
        assertEquals(c1.getRut(), resultado.getRut());
        assertNotNull(resultado.getDirecciones());
        assertEquals(2, resultado.getDirecciones().size());
        assertEquals("Amaru Antú Burdiles Jarpa", resultado.getNombreCompleto());
        assertEquals(c1.getPassword(), resultado.getPassword());

        assertNull(resultado.getNombre());
        assertNull(resultado.getApellido());
        assertNull(resultado.getEstato());
        assertNull(resultado.getNvlPermiso());

        // Verificación
        verify(sesionServicio, times(1)).validacionUsuario(id);
        verify(clienteRepo, times(1)).findById(id);
        verify(sesionServicio, times(0)).validacionEmpleado(id, 4);
    }


    // TEST OBTENER CLIENTE POR ID (EXITOSO: INFO PARA ADMINS)
    @Test
    void testGetClienteByIdAdmin() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion(2L, "Casa mamá", "Paicavi", 810, 69, null, Comuna.CONCEPCION, Region.BIOBIO);

        List<Direccion> dirs = new ArrayList<>();
        dirs.add(d1);
        dirs.add(d2);

        Long idAdmin = 5L;
        Long idBuscar = 1L;

        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", "Antú", "Burdiles", "Jarpa", "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, dirs);
        
        // Configuración
        when(sesionServicio.validacionUsuario(idAdmin)).thenReturn(true);
        when(clienteRepo.findById(idBuscar)).thenReturn(Optional.of(c1));
        when(sesionServicio.validacionEmpleado(idAdmin, 4)).thenReturn(true);

        // Testeo
        GetClienteDTO resultado = clienteServicio.getClienteById(idAdmin, idBuscar);

        // Validación
        assertNotNull(resultado);
        
        assertEquals(c1.getId(), resultado.getIdCliente());
        assertEquals(c1.getCorreo(), resultado.getCorreo());
        assertEquals(c1.getTelefono(), resultado.getTelefono());
        
        assertNull(resultado.getRut());
        assertNull(resultado.getDirecciones());
        assertNull(resultado.getNombreCompleto());
        assertNull(resultado.getPassword());

        assertEquals(c1.getPNombre(), resultado.getNombre());
        assertEquals(c1.getPApellido(), resultado.getApellido());
        assertEquals(c1.getEstado(), resultado.getEstato());
        assertEquals("1", resultado.getNvlPermiso());

        // Verificación
        verify(sesionServicio, times(1)).validacionUsuario(idAdmin);
        verify(clienteRepo, times(1)).findById(idBuscar);
        verify(sesionServicio, times(1)).validacionEmpleado(idAdmin, 4);
    }


    // TEST OBTENER CLIENTE POR ID (EXITOSO: INFO PARA EL SISTEMA)
    @Test
    void testGetClienteByIdSysCall() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion(2L, "Casa mamá", "Paicavi", 810, 69, null, Comuna.CONCEPCION, Region.BIOBIO);

        List<Direccion> dirs = new ArrayList<>();
        dirs.add(d1);
        dirs.add(d2);

        Long idSys = 0L;
        Long idBuscar = 1L;

        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", "Antú", "Burdiles", "Jarpa", "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, dirs);
        
        // Configuración
        when(sesionServicio.validacionUsuario(idSys)).thenReturn(false);
        when(clienteRepo.findById(idBuscar)).thenReturn(Optional.of(c1));
        when(sesionServicio.validacionEmpleado(idSys, 4)).thenReturn(false);

        // Testeo
        GetClienteDTO resultado = clienteServicio.getClienteById(idSys, idBuscar);

        // Validación
        assertNotNull(resultado);
        
        assertEquals(c1.getId(), resultado.getIdCliente());
        assertEquals(c1.getCorreo(), resultado.getCorreo());
        assertEquals(c1.getTelefono(), resultado.getTelefono());
        
        assertNull(resultado.getRut());
        assertNull(resultado.getDirecciones());
        assertNull(resultado.getNombreCompleto());
        assertNull(resultado.getPassword());

        assertEquals(c1.getPNombre(), resultado.getNombre());
        assertEquals(c1.getPApellido(), resultado.getApellido());
        assertEquals(c1.getEstado(), resultado.getEstato());
        assertEquals("1", resultado.getNvlPermiso());

        // Verificación
        verify(sesionServicio, times(0)).validacionUsuario(idSys);
        verify(clienteRepo, times(1)).findById(idBuscar);
        verify(sesionServicio, times(0)).validacionEmpleado(idSys, 4);
    }


    // TEST OBTENER CLIENTE POR ID (EXITOSO: DUEÑO DE LA INFO, SOLO 1ER NOMBRE Y APELLIDO)
    @Test
    void testGetClienteByIdOwnerNotFullName() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion(2L, "Casa mamá", "Paicavi", 810, 69, null, Comuna.CONCEPCION, Region.BIOBIO);

        List<Direccion> dirs = new ArrayList<>();
        dirs.add(d1);
        dirs.add(d2);

        Long id = 1L;

        Cliente c1 = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, dirs);
        
        // Configuración
        when(sesionServicio.validacionUsuario(id)).thenReturn(true);
        when(clienteRepo.findById(id)).thenReturn(Optional.of(c1));
        when(sesionServicio.validacionEmpleado(id, 4)).thenReturn(false);

        // Testeo
        GetClienteDTO resultado = clienteServicio.getClienteById(id, id);

        // Validación
        assertNotNull(resultado);
        
        assertEquals(c1.getId(), resultado.getIdCliente());
        assertEquals(c1.getCorreo(), resultado.getCorreo());
        assertEquals(c1.getTelefono(), resultado.getTelefono());
        
        assertEquals(c1.getRut(), resultado.getRut());
        assertNotNull(resultado.getDirecciones());
        assertEquals(2, resultado.getDirecciones().size());
        assertEquals("Amaru Burdiles", resultado.getNombreCompleto());
        assertEquals(c1.getPassword(), resultado.getPassword());

        assertNull(resultado.getNombre());
        assertNull(resultado.getApellido());
        assertNull(resultado.getEstato());
        assertNull(resultado.getNvlPermiso());

        // Verificación
        verify(sesionServicio, times(1)).validacionUsuario(id);
        verify(clienteRepo, times(1)).findById(id);
        verify(sesionServicio, times(0)).validacionEmpleado(id, 4);
    }


    // TEST OBTENER CLIENTE POR ID (FALLIDO)
    @Test
    void testGetClienteByIdError() {
        // Preparación
        Long id = 1L;
        
        // Configuración
        when(sesionServicio.validacionUsuario(id)).thenThrow(HttpClientErrorException.class);
        when(clienteRepo.findById(id)).thenReturn(null);
        when(sesionServicio.validacionEmpleado(id, 4)).thenReturn(false);

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            clienteServicio.getClienteById(id, id);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionUsuario(id);
        verify(clienteRepo, times(0)).findById(id);
        verify(sesionServicio, times(0)).validacionEmpleado(id, 4);
    }


    // TEST AÑADIR DIRECCIÓN DE ENVÍO (EXITOSO: ETIQUETA PRESENTE)
    @Test
    void testAddDireccionConEtiqueta() {
        // Preparación
        Direccion entrada = new Direccion(null, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion guardada = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);

        Cliente c1 = new Cliente(7L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        
        Long idCli = 7L;

        // Configuración
        when(sesionServicio.validacionCliente(idCli)).thenReturn(true);
        when(clienteRepo.findById(idCli)).thenReturn(Optional.of(c1));
        when(direccionRepo.save(entrada)).thenReturn(guardada);
        when(clienteRepo.save(c1)).thenReturn(c1);

        // Testeo
        Direccion resultado = clienteServicio.addDireccion(idCli, entrada);

        // Validación
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(guardada, resultado);

        assertEquals(entrada.getEtiqueta(), resultado.getEtiqueta());

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(idCli);
        verify(clienteRepo, times(1)).findById(idCli);
        verify(direccionRepo, times(1)).save(entrada);
        verify(clienteRepo, times(1)).save(c1);
    }

  
    // TEST AÑADIR DIRECCIÓN DE ENVÍO (EXITOSO: ETIQUETA ENTRA NULL)
    @Test
    void testAddDireccionEtiquetaNull() {
        // Preparación
        Direccion entrada = new Direccion(null, null, "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion guardada = new Direccion(1L, "Dirección #1", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);

        Cliente c1 = new Cliente(7L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        
        Long idCli = 7L;

        // Configuración
        when(sesionServicio.validacionCliente(idCli)).thenReturn(true);
        when(clienteRepo.findById(idCli)).thenReturn(Optional.of(c1));
        when(direccionRepo.save(entrada)).thenReturn(guardada);
        when(clienteRepo.save(c1)).thenReturn(c1);

        // Testeo
        Direccion resultado = clienteServicio.addDireccion(idCli, entrada);

        // Validación
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(guardada, resultado);

        assertEquals("Dirección #1", resultado.getEtiqueta());

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(idCli);
        verify(clienteRepo, times(1)).findById(idCli);
        verify(direccionRepo, times(1)).save(entrada);
        verify(clienteRepo, times(1)).save(c1);
    }


    // TEST AÑADIR DIRECCIÓN DE ENVÍO (EXITOSO: ETIQUETA ENTRA BLANK)
    @Test
    void testAddDireccionEtiquetaBlank() {
        // Preparación
        Direccion entrada = new Direccion(null, "           ", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion guardada = new Direccion(1L, "Dirección #1", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);

        Cliente c1 = new Cliente(7L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);
        
        Long idCli = 7L;

        // Configuración
        when(sesionServicio.validacionCliente(idCli)).thenReturn(true);
        when(clienteRepo.findById(idCli)).thenReturn(Optional.of(c1));
        when(direccionRepo.save(entrada)).thenReturn(guardada);
        when(clienteRepo.save(c1)).thenReturn(c1);

        // Testeo
        Direccion resultado = clienteServicio.addDireccion(idCli, entrada);

        // Validación
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(guardada, resultado);

        assertEquals("Dirección #1", resultado.getEtiqueta());

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(idCli);
        verify(clienteRepo, times(1)).findById(idCli);
        verify(direccionRepo, times(1)).save(entrada);
        verify(clienteRepo, times(1)).save(c1);
    }


    // TEST AÑADIR DIRECCIÓN DE ENVÍO (EXITOSO: ETIQUETA PRESENTE)
    @Test
    void testAddDireccionError() {
        // Preparación
        Direccion entrada = new Direccion();
        Cliente c1 = new Cliente();
        
        Long idCli = 7L;

        // Configuración
        when(sesionServicio.validacionCliente(idCli)).thenThrow(HttpClientErrorException.class);
        when(clienteRepo.findById(idCli)).thenReturn(Optional.of(c1));
        when(direccionRepo.save(entrada)).thenReturn(entrada);
        when(clienteRepo.save(c1)).thenReturn(c1);

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            clienteServicio.addDireccion(idCli, entrada);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(idCli);
        verify(clienteRepo, times(0)).findById(idCli);
        verify(direccionRepo, times(0)).save(entrada);
        verify(clienteRepo, times(0)).save(c1);
    }


    // TEST OBTENER DIRECCIONES DE CLIENTE (EXITOSO)
    @Test
    void testGetDirecciones() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion(2L, "Casa mamá", "Paicavi", 810, 69, null, Comuna.CONCEPCION, Region.BIOBIO);

        List<Direccion> dirs = new ArrayList<>();
        dirs.add(d1);
        dirs.add(d2);

        Long id = 7L;

        Cliente c7 = new Cliente();
        c7.setId(id);
        c7.setDirecciones(dirs);

        // Configuración
        when(sesionServicio.validacionCliente(id)).thenReturn(true);
        when(clienteRepo.findById(id)).thenReturn(Optional.of(c7));

        // Testeo
        List<GetDireccionDTO> resultado = clienteServicio.getDirecciones(id);

        // Validación
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertEquals(d1.getId(), resultado.getFirst().getIdDireccion());
        assertEquals(d1.getEtiqueta(), resultado.getFirst().getEtiqueta());
        
        assertEquals(d2.getId(), resultado.getLast().getIdDireccion());
        assertEquals(d2.getEtiqueta(), resultado.getLast().getEtiqueta());

        // Verificar
        verify(sesionServicio, times(1)).validacionCliente(id);
        verify(clienteRepo, times(1)).findById(id);
    }


    // TEST OBTENER DIRECCIONES DE CLIENTE (FALLIDO)
    @Test
    void testGetDireccionesError() {
        // Preparación
        Long id = 7L;
        Cliente c7 = new Cliente();

        // Configuración
        when(sesionServicio.validacionCliente(id)).thenThrow(HttpClientErrorException.class);
        when(clienteRepo.findById(id)).thenReturn(Optional.of(c7));

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            clienteServicio.getDirecciones(id);
        });

        // Verificar
        verify(sesionServicio, times(1)).validacionCliente(id);
        verify(clienteRepo, times(0)).findById(id);
    }


    // TEST OBTENER DIRECCIÓN DE ENVÍO (EXITOSO: DIRECCION EXISTE)
    @Test
    void testGetDireccion() {
        // Preparación
        Direccion d1 = new Direccion(1L, "Casa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Long cliente = 7L;
        Long direccion = 1L;

        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenReturn(true);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.of(d1));

        // Testeo
        Direccion resultado = clienteServicio.getDireccion(cliente, direccion);

        // Validación
        assertNotNull(resultado);

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(cliente);
        verify(direccionRepo, times(1)).findById(direccion);
    }


    // TEST OBTENER DIRECCIÓN DE ENVÍO (EXITOSO: DIRECCION NO EXISTE)
    @Test
    void testGetDireccionNotFound() {
        // Preparación
        Long cliente = 7L;
        Long direccion = 1L;

        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenReturn(true);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.empty());

        // Testeo
        Direccion resultado = clienteServicio.getDireccion(cliente, direccion);

        // Validación
        assertNull(resultado);

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(cliente);
        verify(direccionRepo, times(1)).findById(direccion);
    }


    // TEST OBTENER DIRECCIÓN DE ENVÍO (FALLIDO)
    @Test
    void testGetDireccionError() {
        // Preparación
        Long cliente = 7L;
        Long direccion = 1L;

        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenThrow(HttpClientErrorException.class);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.empty());

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            clienteServicio.getDireccion(cliente, direccion);
        });

        // Verificación
        verify(sesionServicio, times(1)).validacionCliente(cliente);
        verify(direccionRepo, times(0)).findById(direccion);
    }


    /* TODO: direccionRepo.findById(id) retorna un valor distinto
       dependiendo en que parte del codigo estes. Como hacer que
       when(...).then(...) haga cambios en el codigo en lugar de
       solo retornar un valor
    */
    // TEST ACTUALIZAR DIRECCIÓN (EXITOSO)
    @Test
    void testUpdateDireccion() {
        // Preparación
        UpdateDirDTO entrada = new UpdateDirDTO();
        entrada.setEtiqueta("Depa");

        Direccion guardada = new Direccion(1L, "Dirección #1", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion d2 = new Direccion();
        Direccion d3 = new Direccion();
        Direccion d4 = new Direccion();

        Direccion actualizada = new Direccion(1L, "Depa", "San Martin", 5, 67, null, Comuna.CONCEPCION, Region.BIOBIO);

        Cliente cli = new Cliente();
        cli.getDirecciones().add(guardada);
        cli.getDirecciones().add(d2);
        cli.getDirecciones().add(d3);
        cli.getDirecciones().add(d4);

        Long cliente = 7L;
        Long direccion = 1L;


        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenReturn(true);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.of(guardada));
        when(clienteRepo.findById(cliente)).thenReturn(Optional.of(cli));

        //when(clienteRepo.save(cli)).thenReturn(null)(guardada = actualizada);

        // Testeo
        Direccion resultado = clienteServicio.updateDireccion(cliente, entrada, direccion);

        // Validación
        assertNotNull(resultado);
        assertEquals(actualizada, resultado);

    }


    // TEST QUITAR DIRECCIÓN DE ENVÍO (EXITOSO)
    @Test
    void testQuitarDireccion() {
        // Preparación
        Direccion d1 = new Direccion();
        d1.setId(1L);

        Direccion d2 = new Direccion();
        d2.setId(2L);

        Cliente c = new Cliente();
        c.setId(7L);
        c.getDirecciones().add(d1);
        c.getDirecciones().add(d2);

        Long cliente = 7L;
        Long direccion = 1L;

        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenReturn(true);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.of(d1));
        when(clienteRepo.findById(cliente)).thenReturn(Optional.of(c));
        when(clienteRepo.save(c)).thenReturn(c);

        // Testeo
        Cliente resultado = clienteServicio.deleteDireccion(cliente, direccion);

        // Validación
        assertNotNull(resultado);
        assertEquals(1, resultado.getDirecciones().size());
        assertEquals(2L, resultado.getDirecciones().getFirst().getId());

        // Verificar
        verify(sesionServicio, times(1)).validacionCliente(cliente);
        verify(direccionRepo, times(1)).findById(direccion);
        verify(clienteRepo, times(1)).findById(cliente);
        verify(clienteRepo, times(1)).save(c);
    }


    // TEST QUITAR DIRECCIÓN DE ENVÍO (FALLIDO: DIRECCION NO ENCONTRADA)
    @Test
    void testQuitarDireccionNotFound() {
        // Preparación
        Cliente c = new Cliente();

        Long cliente = 7L;
        Long direccion = 1L;

        // Configuración
        when(sesionServicio.validacionCliente(cliente)).thenReturn(true);
        when(direccionRepo.findById(direccion)).thenReturn(Optional.empty());
        when(clienteRepo.findById(cliente)).thenReturn(Optional.of(c));
        when(clienteRepo.save(c)).thenReturn(c);

        // Testeo
        Cliente resultado = clienteServicio.deleteDireccion(cliente, direccion);

        // Validación
        assertNull(resultado);

        // Verificar
        verify(sesionServicio, times(1)).validacionCliente(cliente);
        verify(direccionRepo, times(1)).findById(direccion);
        verify(clienteRepo, times(0)).findById(cliente);
        verify(clienteRepo, times(0)).save(c);
    }
}

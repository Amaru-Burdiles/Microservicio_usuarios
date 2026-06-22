package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.enums.Comuna;
import duoc.amaru.usuarios.enums.Region;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class ClienteServiceTest {
    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private ClienteRepo clienteRepo;

    @InjectMocks
    private ClienteServicio clienteServicio;

    @BeforeEach
    void setUp() {
        // Inicializar mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarCliente() {
        // Preparación de Objetos
        Cliente clienteNew = new Cliente(null, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", null, 0, null);
        Cliente clienteSaved = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        // Definición lógica Mockito
        when(usuarioRepo.save(clienteNew)).thenReturn(clienteSaved);

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

    @Test
    void testAddDireccion() {
        // Preparación de Objetos
        Direccion direccionNew = new Direccion(null, "Casa", "San Martin", 3, 108, null, Comuna.CONCEPCION, Region.BIOBIO);
        Direccion direccionSaved = new Direccion(1L, "Casa", "San Martin", 3, 108, null, Comuna.CONCEPCION, Region.BIOBIO);

        Cliente cliA = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "testeando", "9 1234 1234", "activo", 1, null);

        when(clienteRepo.getReferenceById(1L)).thenReturn(cliA);
        // TODO: Continue this
        
    }
}

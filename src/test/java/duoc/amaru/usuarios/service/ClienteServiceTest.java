package duoc.amaru.usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.repository.UsuarioRepo;

public class ClienteServiceTest {
    @Mock
    private UsuarioRepo usuarioRepo;

    @InjectMocks
    private ClienteServicio clienteServicio;

    @BeforeEach
    void setUp() {
        // Inicializar mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarUsuario() {
        Cliente newCliente = new Cliente(null, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "364_04n-4nf9", "9 1234 1234", null, 0, null);
        Cliente savedCliente = new Cliente(1L, "12.123.123-4", "Amaru", null, "Burdiles", null, "a.b@duoc.cl", "364_04n-4nf9", "9 1234 1234", "activo", 1, null);

        when(usuarioRepo.save(newCliente)).thenReturn(savedCliente);

        Cliente resultado = clienteServicio.registrarUsuario(newCliente);

        // El cliente se guardo
        assertNotNull(resultado);

        // El cliente guardado por el Servicio es igual al Mock
        assertEquals(savedCliente, resultado);

        // Los valores rellenados automaticamante son los esperados
        assertEquals(1L, resultado.getId());
        assertEquals(1, resultado.getNvlPermiso());
        assertEquals("activo", resultado.getEstado());

        verify(usuarioRepo, times(1)).save(newCliente);
    }
}

package duoc.amaru.usuarios.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.ClienteRepo;

public class ClienteServiceTest {
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
    void testRegistrarUsuario() {
        // TODO: Implementar prueba para registrar un nuevo cliente
        //Usuario nuevoCliente = new Cliente;
}

package duoc.amaru.usuarios.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import duoc.amaru.usuarios.service.SesionServicio;

@WebMvcTest(SesionControlador.class)
public class SesionControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SesionServicio sesionServicio;

    @Test
    void validarEmpleado_RetornaTrue() throws Exception {
        when(sesionServicio.validacionEmpleado(1L, 2)).thenReturn(true);

        mockMvc.perform(get("/api/v1/sesiones/validacion-empleado/exe:1/filter:2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sesionServicio, times(1)).validacionEmpleado(1L, 2);
    }

    @Test
    void validarEmpleado_RetornaFalse() throws Exception {
        when(sesionServicio.validacionEmpleado(99L, 5)).thenReturn(false);

        mockMvc.perform(get("/api/v1/sesiones/validacion-empleado/exe:99/filter:5"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(sesionServicio, times(1)).validacionEmpleado(99L, 5);
    }

    @Test
    void validarCliente_RetornaTrue() throws Exception {
        when(sesionServicio.validacionCliente(10L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/sesiones/validacion-cliente/exe:10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sesionServicio, times(1)).validacionCliente(10L);
    }

    @Test
    void validarCliente_RetornaFalse() throws Exception {
        when(sesionServicio.validacionCliente(99L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/sesiones/validacion-cliente/exe:99"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(sesionServicio, times(1)).validacionCliente(99L);
    }

    @Test
    void validarUsuario_RetornaTrue() throws Exception {
        when(sesionServicio.validacionUsuario(5L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/sesiones/validacion-usuario/exe:5"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sesionServicio, times(1)).validacionUsuario(5L);
    }

    @Test
    void validarUsuario_RetornaFalse() throws Exception {
        when(sesionServicio.validacionUsuario(100L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/sesiones/validacion-usuario/exe:100"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(sesionServicio, times(1)).validacionUsuario(100L);
    }

    @Test
    void getIsLoggedIn_RetornaTrue() throws Exception {
        when(sesionServicio.isLoggedIn(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/sesiones/is-logged-in/user:1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(sesionServicio, times(1)).isLoggedIn(1L);
    }

    @Test
    void getIsLoggedIn_RetornaFalse() throws Exception {
        when(sesionServicio.isLoggedIn(50L)).thenReturn(false);

        mockMvc.perform(get("/api/v1/sesiones/is-logged-in/user:50"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(sesionServicio, times(1)).isLoggedIn(50L);
    }
}
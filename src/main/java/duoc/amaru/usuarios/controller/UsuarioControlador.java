package duoc.amaru.usuarios.controller;

import duoc.amaru.usuarios.service.EmpleadoServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.usuarios.DTO.LogInDTO;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.service.ClienteServicio;
import duoc.amaru.usuarios.service.UsuarioServicio;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/eco_market_spa")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private EmpleadoServicio empleadoServicio;
    
    // OBTENER USUARIOS (TESTING)
    @GetMapping
    public ResponseEntity<?> getUsuarios() {
        List<Usuario> users = usuarioServicio.readAllUsers();

        if (users.isEmpty())
            return ResponseEntity.ok("No hay usuarios registrados");
        return ResponseEntity.ok(users);
    }

    // REGISTRAR CLIENTE
    @PostMapping("/signin")
    public ResponseEntity<?> postRegistrarCliente(@Valid @RequestBody Cliente newCliente) {
        return clienteServicio.registrarUsuario(newCliente);
    }

    // REGISTRAR EMPLEADO
    @PostMapping("/signin/{id}")
    public ResponseEntity<?> postRegistrarEmpleado(@Valid @RequestBody Empleado newEmpleado, @PathVariable Long id) {
        return empleadoServicio.registrarUsuario(id, newEmpleado);
    }
    
    
    // INICIAR SESION
    @PostMapping("/login")
    public ResponseEntity<?> postInicioSesion(@Valid @RequestBody LogInDTO logInDTO) {
        return usuarioServicio.iniciarSesion(logInDTO.getCorreo(), logInDTO.getPassword());
    }

    // CERRAR SESION
    @GetMapping("/logout/{id}")
    public ResponseEntity<?> getCerrarSesion(@PathVariable Long id) {
        return usuarioServicio.cerrarSesion(id);
    }
    
    
}

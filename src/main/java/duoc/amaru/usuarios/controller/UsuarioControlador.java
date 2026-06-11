package duoc.amaru.usuarios.controller;

import duoc.amaru.usuarios.service.EmpleadoServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.usuarios.dto.LogInDTO;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Empleado;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.service.ClienteServicio;
import duoc.amaru.usuarios.service.UsuarioServicio;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/usuarios")
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
    
    // ACTUALIZAR PERMISOS DE USUARIO
    @PutMapping("/user{userId}:config/access{nvl}/{executorId}")
    public ResponseEntity<?> putConfigUsuario(@PathVariable Long userId, @PathVariable int nvl, @PathVariable Long executor) {
        return empleadoServicio.updateUsusario(userId, nvl, executor);
    }

    // ACTUALIZAR CARGO DE EMPLEADO
    @PutMapping("/user{userId}:config/cargo:{cargo}/{executorId}")
    public ResponseEntity<?> putCargoEmpleado(@PathVariable Long empId, @PathVariable String cargo, @PathVariable Long executor) {
        return empleadoServicio.updateUsuario(empId, cargo, executor);
    }

    // DESACTIVAR USUARIO
    @PutMapping("/user{userId}:desactivar/{executorId}")
    public ResponseEntity<?> putDesactivarUser(@PathVariable Long userId, @PathVariable Long executorId) {
        return empleadoServicio.desactivarUser(userId, executorId);
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/user{userId}:eliminar/{executorId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long userId, @PathVariable Long executorId) {
        return empleadoServicio.eliminarUser(userId, executorId);
    }
}

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

import duoc.amaru.usuarios.dto.GetClienteDTO;
import duoc.amaru.usuarios.dto.GetDireccionDTO;
import duoc.amaru.usuarios.dto.LogInDTO;
import duoc.amaru.usuarios.dto.UpdateDirDTO;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
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
        Cliente serviceReply = clienteServicio.registrarUsuario(newCliente);
        if (serviceReply == null)
            return ResponseEntity.badRequest().body("El correo ingresado ya está registrado");
        
        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }

    // REGISTRAR EMPLEADO
    @PostMapping("/signin/{id}")
    public ResponseEntity<?> postRegistrarEmpleado(@Valid @RequestBody Empleado newEmpleado, @PathVariable Long id) {
        Empleado reply = empleadoServicio.registrarUsuario(id, newEmpleado);

        if (reply == null)
            return ResponseEntity.status(400).body("El correo ingresado ya está registrado");

        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }
    
    
    // INICIAR SESION
    @PostMapping("/login")
    public ResponseEntity<?> postInicioSesion(@Valid @RequestBody LogInDTO logInDTO) {
        Usuario reply = usuarioServicio.iniciarSesion(logInDTO.getCorreo(), logInDTO.getPassword());

        if (reply == null)
            return ResponseEntity.badRequest().body("Su sesión ya está iniciada");

        String respuesta = "Iniciaste sesión como <"+ reply.getPNombre() +"> con Id: #"+ reply.getId();
        return ResponseEntity.ok(respuesta);
    }

    // CERRAR SESION
    @GetMapping("/logout/{id}")
    public ResponseEntity<?> getCerrarSesion(@PathVariable Long id) {
        Usuario reply = usuarioServicio.cerrarSesion(id);

        if (reply == null)
            return ResponseEntity.status(401).body("Usuario no autenticado. La sesión ya está cerrada");

        String respuesta = "Se cerró la sesión de "+ reply.getPNombre();
        return ResponseEntity.status(200).body(respuesta);
    }
    
    // ACTUALIZAR PERMISOS DE USUARIO
    @PutMapping("/user{userId}:config/access{nvl}/{executorId}")
    public ResponseEntity<?> putConfigUsuario(@PathVariable Long userId, @PathVariable int nvl, @PathVariable Long executor) {
        Usuario reply = empleadoServicio.updateUsusario(userId, nvl, executor);

        if (reply == null)
            return ResponseEntity.status(400).body("Nuevo nivel de acceso fuera de rango");

        return ResponseEntity.ok("Permisos actualizados para usuario "+ reply.getPNombre() +" "+ reply.getPApellido());
    }

    // ACTUALIZAR CARGO DE EMPLEADO
    @PutMapping("/user{userId}:config/cargo:{cargo}/{executorId}")
    public ResponseEntity<?> putCargoEmpleado(@PathVariable Long empId, @PathVariable String cargo, @PathVariable Long executor) {
        Empleado reply = empleadoServicio.updateUsuario(empId, cargo, executor);

        if (reply == null)
            return ResponseEntity.status(400).body("Cargo inválido");

        return ResponseEntity.ok("Cargo actualizado para empleado "+ reply.getPNombre() +" "+ reply.getPApellido());
    }

    // AGREGAR DIRECCION DE ENVIO A CLIENTE
    @PostMapping("/user{userId}/direccion/add")
    public ResponseEntity<?> postDireccionEnvio(@Valid @RequestBody Direccion direccion, @PathVariable Long userId) {
        Direccion resultado = clienteServicio.addDireccion(userId, direccion);
        GetClienteDTO cli = clienteServicio.getClienteById(0L, userId);
        
        String reply = "Dirección añadida correctamente a "+ cli.getNombre() +" como \""+ resultado.getEtiqueta() +'\"';
        return ResponseEntity.status(201).body(reply);
    }

    // OBTENER DIRECCIONES DE ENVIO DE CLIENTE
    @GetMapping("/user{userId}/direcciones")
    public ResponseEntity<?> getDireccionesEnvio(@PathVariable Long userId) {
        List<GetDireccionDTO> reply = clienteServicio.getDirecciones(userId);
        if (reply.isEmpty())
            return ResponseEntity.ok("No hay direcciones de envío guardadas");

        int cant = reply.size();
        String respuesta = cant +(cant == 1 ? " dirección de envío guardada:\n" : " direcciones de envío guardadas:\n") + reply;
        return ResponseEntity.ok(respuesta);
    }

    // OBTENER DETALLE DE DIRECCION DE ENVIO DE CLIENTE
    @GetMapping("/user{userId}/direccion/{dirId}")
    public ResponseEntity<?> getDireccionEnvio(@PathVariable Long userId, @PathVariable Long dirId) {
        Direccion reply = clienteServicio.getDireccion(userId, dirId);
        if (reply == null)
            return ResponseEntity.status(404).body("Dirección no encontrada");
        return ResponseEntity.ok(reply);
    }

    // ACTUALIZAR DIRECCION DE ENVIO DE CLIENTE
    @PutMapping("/user{userId}/direccion{dirId}/update")
    public ResponseEntity<?> putDireccionEnvio(@Valid @RequestBody UpdateDirDTO direccion, @PathVariable Long userId, @PathVariable Long dirId) {
        Direccion reply = clienteServicio.updateDireccion(userId, direccion, dirId);
        if (reply == null)
            return ResponseEntity.status(404).body("Dirección no encontrada");
        return ResponseEntity.ok("Dirección actualizada correctamente");
    }

    // QUITAR DIRECCION DE ENVIO DE CLIENTE
    @DeleteMapping("/user{userId}/direccion{dirId}/del")
    public ResponseEntity<?> deleteDireccionEnvio(@PathVariable Long userId, @PathVariable Long dirId) {
        Cliente reply = clienteServicio.deleteDireccion(userId, dirId);
        if (reply != null)
            return ResponseEntity.ok("Dirección eliminada correctamente");

        return ResponseEntity.status(404).body("Dirección no encontrada");
    }

    // DESACTIVAR USUARIO
    @PutMapping("/user{userId}:desactivar/{executorId}")
    public ResponseEntity<?> putDesactivarUser(@PathVariable Long userId, @PathVariable Long executorId) {
        Usuario reply = empleadoServicio.desactivarUser(userId, executorId);
        return ResponseEntity.ok("La cuenta de "+ reply.getPNombre() +" "+ reply.getPApellido() +" ha sido desactivada"); 
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/user{userId}:eliminar/{executorId}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long userId, @PathVariable Long executorId) {
        empleadoServicio.eliminarUser(userId, executorId);

        return ResponseEntity.ok("Usuario con Id #"+ userId +" eliminado");
    }
}

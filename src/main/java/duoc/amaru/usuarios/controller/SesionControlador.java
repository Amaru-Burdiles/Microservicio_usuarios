package duoc.amaru.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.usuarios.service.SesionServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/v1/sesiones")
public class SesionControlador {
    @Autowired
    private SesionServicio sesionServicio;

    // VALIDAR EMPLEADOS Y SU NIVEL DE ACCESO
    @GetMapping("/validacion-empleado/exe:{executorId}/filter:{lvl}")
    public ResponseEntity<?> validarEmpleado(@PathVariable Long ejecutor, @PathVariable int lvl) {
        return sesionServicio.validacionEmpleado(ejecutor, lvl);
    }

    // VALIDAR CLIENTES
    @GetMapping("/validacion-cliente/exe:{userId}")
    public ResponseEntity<?> validarCliente(@PathVariable Long userId) {
        return sesionServicio.validacionCliente(userId);
    }
    
    // VALIDAR USUARIOS EN GENERAL
    @GetMapping("/validacion-usuario/exe:{userId}")
    public ResponseEntity<?> validarUsuario(@PathVariable Long userId) {
        return sesionServicio.validacionUsuario(userId);
    }
    
    // VALIDAR USUARIO HAYA INICIADO SESION
    @GetMapping("/is-logged-in/user:{userId}")
    public boolean getIsLoggedIn(@PathVariable Long userId) {
        return sesionServicio.isLoggedIn(userId);
    }
}

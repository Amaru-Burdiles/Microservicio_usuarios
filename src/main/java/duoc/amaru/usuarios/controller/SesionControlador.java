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
    @GetMapping("/access-validation/exe:{executorId}/filter:{lvl}")
    public ResponseEntity<?> getAccess(@PathVariable Long ejecutor, @PathVariable int lvl) {
        return sesionServicio.accessValidation(ejecutor, lvl);
    }

    // VALIDAR QUE EL USUARIO HAYA INICIADO SESION
    @GetMapping("/is-logged-in/user:{userId}")
    public boolean getIsLoggedIn(@PathVariable Long userId) {
        return sesionServicio.isLoggedIn(userId);
    }

    // VALIDAR CLIENTES
    @GetMapping("/validacion-cliente/user:{userId}")
    public ResponseEntity<?> getAccess(@PathVariable Long userId) {
        return sesionServicio.validacionCliente(userId);
    }
    
    
}

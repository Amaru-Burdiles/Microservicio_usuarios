package duoc.amaru.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.usuarios.DTO.LogInDTO;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.service.UsuarioServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/eco_market_spa")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String getPlaceholder() {
        return "Pagina principal";
    }

    // REGISTRAR USUARIO
    @PostMapping("/signin")
    public ResponseEntity<?> postRegistrarUsuario(@RequestBody Usuario nuevoUsuario) {
        return usuarioServicio.registrarUsuario(nuevoUsuario);
    }
    

    // INICIAR SESION
    @PostMapping("/login")
    public ResponseEntity<?> postInicioSesion(@RequestBody LogInDTO logInDTO) {
        return usuarioServicio.iniciarSesion(logInDTO.getCorreo(), logInDTO.getPassword());
    }

    // CERRAR SESION
    @GetMapping("/logout")
    public ResponseEntity<?> getCerrarSesion() {
        return ResponseEntity.ok("Cerraste sesión exitosamente");
    }
    
    
}

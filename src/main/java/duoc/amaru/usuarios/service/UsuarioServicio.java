package duoc.amaru.usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class UsuarioServicio {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private SesionServicio sesionServicio;

    // INICIAR SESION
    public ResponseEntity<?> iniciarSesion(String correo, String password) {
        // Quien intenta iniciar sesión
        Usuario logging = usuarioRepo.findByCorreo(correo); 
        
        // Determinar si el correo está registrado
        if (logging == null)
            return ResponseEntity.status(404).body("El correo ingresado no está registrado");
        
        // Contraseña incorrecta
        if (!logging.getPassword().equals(password))
            return ResponseEntity.status(401).body("La contraseña es incorrecta");
        
        // Id del Usuario
        Long id = logging.getId();

        // Validación sesión ya iniciada?
        if (sesionServicio.isLoggedIn(id))
            return ResponseEntity.status(400).body("Su sesión ya está iniciada");

        // Inicio de sesión y mensaje al controlador
        sesionServicio.logIn(id);
        String reply = "Iniciaste sesión como <"+ logging.getPNombre() +"> con Id: #"+ id;
        return ResponseEntity.ok(reply);
    }

    public List<Usuario> readAllUsers() {
        return usuarioRepo.findAll();
    }

    // CERRAR SESION
    public ResponseEntity<?> cerrarSesion(Long id) {
        // Quien intenta cerrar sesion
        Optional<Usuario> user = usuarioRepo.findById(id);
        
        // Validacion Id de usuario existe
        if (!user.isPresent())
            return ResponseEntity.status(404).body("Usuario no encontrado");

        // Validación el usuario tiene la sesión iniciada?
        if (!sesionServicio.isLoggedIn(id))
            return ResponseEntity.status(401).body("Usuario no autenticado. La sesión ya está cerrada");
        
        // Cierre de sesión y respuesta al controlador
        sesionServicio.logOut(id);
        String reply = "Se cerró la sesión de "+ user.get().getPNombre();
        return ResponseEntity.status(200).body(reply);
    }
}

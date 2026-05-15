package duoc.amaru.usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class UsuarioServicio {
    @Autowired
    private UsuarioRepo usuarioRepo;

    // INICIAR SESION
    public ResponseEntity<?> iniciarSesion(String correo, String password) {
        // Quien intenta iniciar sesión
        Usuario whoIsLogging = null;

        // Buscar usuario por correo
        for (Usuario user : usuarioRepo.findAll()) {
            if (user.getCorreo().equalsIgnoreCase(correo)) {
                whoIsLogging = user;
                break;
            }
        }

        // Determinar si el correo está registrado
        if (whoIsLogging == null)
            return ResponseEntity.status(401).body("El correo ingresado no está registrado");

        // Contraseña correcta
        if (whoIsLogging.getPassword().equals(password))
            return ResponseEntity.ok("Iniciaste sesión como " + whoIsLogging.getPNombre());

        // Contraseña incorrecta
        return ResponseEntity.status(401).body("La contraseña es incorrecta");
    }

    // CERRAR SESION
    // TODO: Implementar cierre de sesion
}

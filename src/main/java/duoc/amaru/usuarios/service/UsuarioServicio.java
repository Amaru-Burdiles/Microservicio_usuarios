package duoc.amaru.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.error.exceptions.DisabledUserException;
import duoc.amaru.usuarios.error.exceptions.EmailNotFoundException;
import duoc.amaru.usuarios.error.exceptions.NotSignedInException;
import duoc.amaru.usuarios.error.exceptions.WrongPasswordException;
import duoc.amaru.usuarios.model.Usuario;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class UsuarioServicio {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private SesionServicio sesionServicio;

    // INICIAR SESION
    public Usuario iniciarSesion(String correo, String password) {
        // Quien intenta iniciar sesión
        Usuario logging = usuarioRepo.findByCorreo(correo); 
        
        // Determinar si el correo está registrado
        if (logging == null)
            throw new EmailNotFoundException();
        
        // Contraseña incorrecta
        if (!logging.getPassword().equals(password))
            throw new WrongPasswordException();

        // Validación usuario desactivado
        if (logging.getEstado().equals("Desactivado"))
            throw new DisabledUserException();
        
        // Id del Usuario
        Long id = logging.getId();

        // Validación sesión ya iniciada?
        if (sesionServicio.isLoggedIn(id))
            return null;

        // Inicio de sesión y mensaje al controlador
        sesionServicio.logIn(id);
        return logging;
    }

    public List<Usuario> readAllUsers() {
        return usuarioRepo.findAll();
    }

    // CERRAR SESION
    public Usuario cerrarSesion(Long id) {
        // Quien intenta cerrar sesion
        Usuario user = usuarioRepo.findById(id).orElse(null);
        
        // Validacion Id de usuario existe
        if (user == null)
            throw new NotSignedInException();

        // Validación el usuario tiene la sesión iniciada?
        if (!sesionServicio.isLoggedIn(id))
            return null;
        
        // Cierre de sesión y respuesta al controlador
        sesionServicio.logOut(id);
        return user;
    }
}

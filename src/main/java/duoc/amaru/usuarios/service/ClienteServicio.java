package duoc.amaru.usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class ClienteServicio {
    @Autowired
    private ClienteRepo clienteRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;


    // REGISTRO DE CLIENTES
    public ResponseEntity<?> registrarUsuario(Cliente nuevoCliente) {
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(nuevoCliente.getCorreo()))
            return ResponseEntity.status(400).body("El correo ingresado ya está registrado");

        // Config usuario nuevo
        nuevoCliente.setEstado("activo");
        nuevoCliente.setNvlPermiso(1);

        // Guardar usuario nuevo
        usuarioRepo.save(nuevoCliente);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    
    // AÑADIR DIRECCION DE ENVIO
    public ResponseEntity<?> addDireccion(Long id, Direccion dir) {
        if (!usuarioRepo.existsById(id))
            return ResponseEntity.status(400).body("Cliente no encontrado");

        Cliente cli = clienteRepo.getReferenceById(id);

        if (dir.getEtiqueta().isBlank()) {
            String tag = "Dirección #" + cli.getTagId().getAndIncrement();
            dir.setEtiqueta(tag);
        }

        cli.getDirecciones().add(dir);
        return ResponseEntity.ok("Dirección añadida correctamente a "+ cli.getPNombre() +" como '"+ dir.getEtiqueta() +'\'');
    }
}

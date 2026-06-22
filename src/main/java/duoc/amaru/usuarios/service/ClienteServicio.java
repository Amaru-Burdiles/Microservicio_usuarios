package duoc.amaru.usuarios.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.dto.GetDireccionDTO;
import duoc.amaru.usuarios.dto.UpdateDirDTO;
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

    @Autowired
    private SesionServicio sesionServicio;

    // REGISTRO DE CLIENTES
    public Cliente registrarUsuario(Cliente nuevoCliente) {
        // Verificar que el correo no esté registrado
        if (usuarioRepo.existsByCorreo(nuevoCliente.getCorreo()))
            return null;

        // Config usuario nuevo
        nuevoCliente.setEstado("activo");
        nuevoCliente.setNvlPermiso(1);

        // Guardar usuario nuevo
        return usuarioRepo.save(nuevoCliente);
    }

    
    // AÑADIR DIRECCION DE ENVIO
    public ResponseEntity<?> addDireccion(Long id, Direccion dir) {
        // Verifica que el cliente existe
        ResponseEntity<?> reply = sesionServicio.validacionCliente(id);
        if (reply != null) {
            return reply;
        }

        // Obtiene el cliente al que pertenecera la direccion
        Cliente cli = clienteRepo.getReferenceById(id);

        // Si la etiqueta de direccion esta vacía, genera una por defecto
        if (dir.getEtiqueta().isBlank()) {
            String tag = "Dirección #" + cli.getTagId().getAndIncrement();
            dir.setEtiqueta(tag);
        }

        // Agrega la direccion y genera respuesta para controlador
        cli.getDirecciones().add(dir);
        String respuesta = "Dirección añadida correctamente a "+ cli.getPNombre() +" como '"+ dir.getEtiqueta() +'\'';
        return ResponseEntity.status(201).body(respuesta);
    }

    // OBTENER DIRECCIONES DE ENVIO DE UN CLIENTE
    public ResponseEntity<?> getDirecciones(Long userId) {
        // Verifica que el cliente existe
        ResponseEntity<?> reply = sesionServicio.validacionCliente(userId);
        if (reply != null) {
            return reply;
        }
        // Obtiene al cliente
        Cliente cli = clienteRepo.getReferenceById(userId);
        
        // Crear resumen de direcciones
        List<GetDireccionDTO> direcciones = new ArrayList<>();
        for (Direccion dir : cli.getDirecciones()) {
            GetDireccionDTO dirDTO = new GetDireccionDTO(dir.getId(), dir.getEtiqueta());
            direcciones.add(dirDTO);
        }
        return ResponseEntity.ok(direcciones);
    }

    // OBTENER DIRECCION DE ENVIO DE UN CLIENTE
    public ResponseEntity<?> getDireccion(Long userId, Long dirId) {
        // Verifica que el cliente existe
        ResponseEntity<?> reply = sesionServicio.validacionCliente(userId);
        if (reply != null) {
            return reply;
        }

        // Buscar direccion por id
        Direccion dir = clienteRepo.findDireccionById(dirId);
        if (dir != null) {
            return ResponseEntity.ok(dir);
        }
        return ResponseEntity.status(404).body("Dirección no encontrada");
    }

    // ACTUALIZAR DIRECCION DE ENVIO DE UN CLIENTE
    public ResponseEntity<?> updateDireccion(Long userId, UpdateDirDTO direccion, Long dirId) {
        // Verifica que el cliente existe
        ResponseEntity<?> reply = sesionServicio.validacionCliente(userId);
        if (reply != null) {
            return reply;
        }

        // Buscar direccion por id
        Direccion dir = clienteRepo.findDireccionById(dirId);
        if (dir == null) {
            return ResponseEntity.status(404).body("Dirección no encontrada");
        }

        // Validar que que hayan cambios y actualizar
        boolean hayCambios = false;
        if (direccion.getEtiqueta().isBlank() || !direccion.getEtiqueta().equals(dir.getEtiqueta())) {
            hayCambios = true;
            dir.setEtiqueta(direccion.getEtiqueta());
        }
        if (direccion.getCalle().isBlank() || !direccion.getCalle().equals(dir.getCalle())) {
            hayCambios = true;
            dir.setCalle(direccion.getCalle());
        }
        if (direccion.getNumCalle() == 0 || direccion.getNumCalle() != dir.getNCalle()) {
            hayCambios = true;
            dir.setNCalle(direccion.getNumCalle());
        }
        if (direccion.getNumCasaDpto() == 0 || direccion.getNumCasaDpto() != dir.getNCasaDpto()) {
            hayCambios = true;
            dir.setNCasaDpto(direccion.getNumCasaDpto());
        }
        if (direccion.getDetalle().isBlank() || !direccion.getDetalle().equals(dir.getDetalle())) {
            hayCambios = true;
            dir.setDetalle(direccion.getDetalle());
        }
        if (direccion.getComuna() != null || direccion.getComuna() != dir.getComuna()) {
            hayCambios = true;
            dir.setComuna(direccion.getComuna());
        }
        if (direccion.getRegion() != null || direccion.getRegion() != dir.getRegion()) {
            hayCambios = true;
            dir.setRegion(direccion.getRegion());
        }

        if (!hayCambios) {
            return ResponseEntity.status(400).body("No hay cambios para actualizar");
        }

        // Commit cambios y generar respuesta para controlador
        clienteRepo.save(clienteRepo.getReferenceById(userId));
        return ResponseEntity.ok("Dirección actualizada correctamente");
    }

    // QUITAR DIRECCION DE ENVIO DE UN CLIENTE
    public ResponseEntity<?> deleteDireccion(Long userId, Long dirId) {
        // Verifica que el cliente existe
        ResponseEntity<?> reply = sesionServicio.validacionCliente(userId);
        if (reply != null) {
            return reply;
        }

        // Buscar direccion por id
        Direccion dir = clienteRepo.findDireccionById(dirId);
        if (dir == null) {
            return ResponseEntity.status(404).body("Dirección no encontrada");
        }

        // Eliminar direccion y generar respuesta para controlador
        Cliente cli = clienteRepo.getReferenceById(userId);
        cli.getDirecciones().remove(dir);
        clienteRepo.save(cli);
        return ResponseEntity.ok("Dirección eliminada correctamente");
    }
}

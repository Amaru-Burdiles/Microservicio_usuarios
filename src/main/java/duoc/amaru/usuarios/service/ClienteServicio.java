package duoc.amaru.usuarios.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.usuarios.dto.GetClienteDTO;
import duoc.amaru.usuarios.dto.GetClientesDTO;
import duoc.amaru.usuarios.dto.GetDireccionDTO;
import duoc.amaru.usuarios.dto.UpdateDirDTO;
import duoc.amaru.usuarios.error.exceptions.SinCambiosException;
import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;
import duoc.amaru.usuarios.repository.ClienteRepo;
import duoc.amaru.usuarios.repository.DireccionRepo;
import duoc.amaru.usuarios.repository.UsuarioRepo;

@Service
public class ClienteServicio {
    @Autowired
    private ClienteRepo clienteRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private DireccionRepo direccionRepo;

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


    // OBTENER CLIENTES REGISTRADOS
    public List<GetClientesDTO> getAllClientes(Long idEjecutor) {
        // Id = 0 representa operaciones y peticiones del sistema mismo
        if (idEjecutor > 0) 
            sesionServicio.validacionEmpleado(idEjecutor, 2);

        // Summarize list
        List<GetClientesDTO> summCli = new ArrayList<>();
        
        // Full list
        List<Cliente> fullCli = clienteRepo.findAll();
        for (Cliente cli : fullCli) {
            String nombApel = cli.getPNombre() +' '+ cli.getPApellido();
            summCli.add(new GetClientesDTO(cli.getId(), nombApel, cli.getCorreo(), cli.getEstado(), cli.getNvlPermiso()));
        }

        return summCli;
    }


    // OBTENER CLIENTE POR ID
    public GetClienteDTO getClienteById(Long idEjecutor, Long idBuscar) {
        if (idEjecutor > 0) {
            sesionServicio.validacionUsuario(idEjecutor);
        }

        // Obtiene al cliente completo
        Cliente full = clienteRepo.findById(idBuscar).get();
        // Crea un cliente resumen (dto)
        GetClienteDTO dto = new GetClienteDTO();

        /* Rellena dto con info general
           Ambos cliente dueño y admin puede ver esta info */
        dto.setIdCliente(full.getId());
        dto.setCorreo(full.getCorreo());
        dto.setTelefono(full.getTelefono());
        
        // Validar idEjecutor es dueño de la info
        if (idBuscar == idEjecutor) {
            /* Rellena con info sensible
            solo visible para cliente dueño */
            dto.setRut(full.getRut());
            dto.setDirecciones(full.getDirecciones());
            dto.setPassword(full.getPassword());
            
            // Concatena los nombres
            String fullName = full.getPNombre() + (full.getSNombre() == null ? ' ' : ' '+full.getSNombre() +' ');
            // A eso le concatena los apellidos
            fullName += full.getPApellido() + (full.getSApellido() == null ? "" : ' '+full.getSApellido());
            dto.setNombreCompleto(fullName);

            return dto;
        }
        // Validar idEjecutor es admin o system
        if (idEjecutor > 0) {
            sesionServicio.validacionEmpleado(idEjecutor, 4);
        }

        // Rellena con info útil para administradores
        dto.setNombre(full.getPNombre());
        dto.setApellido(full.getPApellido());
        dto.setEstato(full.getEstado());
        dto.setNvlPermiso(String.valueOf(full.getNvlPermiso()));

        return dto;
    }

    
    // AÑADIR DIRECCION DE ENVIO
    public Direccion addDireccion(Long id, Direccion dir) {
        // Verifica que el cliente existe
        sesionServicio.validacionCliente(id);

        // Obtiene el cliente al que pertenecera la direccion
        Cliente cli = clienteRepo.findById(id).get();

        // Si la etiqueta de direccion esta vacía, genera una por defecto
        if (dir.getEtiqueta() == null || dir.getEtiqueta().isBlank()) {
            String tag = "Dirección #" + cli.getTagId().getAndIncrement();
            dir.setEtiqueta(tag);
        }

        // Agrega la direccion a la lista de Cliente
        cli.getDirecciones().add(direccionRepo.save(dir));
        clienteRepo.save(cli);

        //  y genera respuesta para controlador
        return cli.getDirecciones().getLast();
    }


    // OBTENER DIRECCIONES DE ENVIO DE UN CLIENTE
    public List<GetDireccionDTO> getDirecciones(Long userId) {
        // Verifica que el cliente existe
        sesionServicio.validacionCliente(userId);

        // Obtiene al cliente
        Cliente cli = clienteRepo.findById(userId).get();
        
        // Crear resumen de direcciones
        List<GetDireccionDTO> direcciones = new ArrayList<>();
        for (Direccion dir : cli.getDirecciones()) {
            GetDireccionDTO dirDTO = new GetDireccionDTO(dir.getId(), dir.getEtiqueta());
            direcciones.add(dirDTO);
        }
        return direcciones;
    }


    // OBTENER DIRECCION DE ENVIO DE UN CLIENTE
    public Direccion getDireccion(Long userId, Long dirId) {
        // Verifica que el cliente existe
        sesionServicio.validacionCliente(userId);

        // Buscar direccion por id
        Direccion dir = direccionRepo.findById(dirId).orElse(null);
        if (dir != null) {
            return dir;
        }
        return null;
    }

    // ACTUALIZAR DIRECCION DE ENVIO DE UN CLIENTE
    public Direccion updateDireccion(Long userId, UpdateDirDTO nueva, Long dirId) {
        // Verifica que el cliente existe
        sesionServicio.validacionCliente(userId);

        // Buscar y validar direccion existe por id
        Direccion former = direccionRepo.findById(dirId).orElse(null);
        if (former == null) {
            return null;
        }

        // Transformar dirección actual a resumen (dto)
        UpdateDirDTO formerDTO = new UpdateDirDTO();
        formerDTO.setEtiqueta(former.getEtiqueta());
        formerDTO.setCalle(former.getCalle());
        formerDTO.setNumCalle(former.getNCalle());
        formerDTO.setNumCasaDpto(former.getNCasaDpto());
        formerDTO.setDetalle(former.getDetalle());
        formerDTO.setComuna(former.getComuna());
        formerDTO.setRegion(former.getRegion());

        // Validar que que hayan cambios (nueva != formerDTO)
        if (nueva.equals(formerDTO))
            throw new SinCambiosException();

        // Obtener cliente al que pertenece la direccion
        Cliente cli = clienteRepo.findById(userId).get();
        
        // Encontrar la posicion de la direccion en la lista
        int i = cli.getDirecciones().indexOf(former);
        
        // Aplicar cambios a former
        if (nueva.getEtiqueta() != null)
            former.setEtiqueta(nueva.getEtiqueta());

        if (nueva.getCalle() != null)
            former.setCalle(nueva.getCalle());

        if (nueva.getNumCasaDpto() != 0)
            former.setNCasaDpto(nueva.getNumCasaDpto());
        
        if (nueva.getComuna() != null)
            former.setComuna(nueva.getComuna());
        
        if (nueva.getRegion() != null)
            former.setRegion(nueva.getRegion());
        
        former.setNCalle(nueva.getNumCalle());
        former.setDetalle(nueva.getDetalle());
        
        // Reemplazar en la lista
        cli.getDirecciones().set(i, former);
        
        // Guardar cambios y respuesta al controlador
        clienteRepo.save(cli);
        return direccionRepo.findById(dirId).get();
    }

    // QUITAR DIRECCION DE ENVIO DE UN CLIENTE
    public Cliente deleteDireccion(Long userId, Long dirId) {
        // Verifica que el cliente existe
        sesionServicio.validacionCliente(userId);

        // Buscar direccion por id
        Direccion dir = direccionRepo.findById(dirId).orElse(null);
        if (dir == null) {
            return null;
        }

        // Eliminar direccion y respuesta al controlador
        Cliente cli = clienteRepo.findById(userId).get();
        cli.getDirecciones().remove(dir);
        return clienteRepo.save(cli);
    }
}

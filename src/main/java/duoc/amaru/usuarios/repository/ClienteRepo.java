package duoc.amaru.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.usuarios.model.Cliente;
import duoc.amaru.usuarios.model.Direccion;

public interface ClienteRepo extends JpaRepository<Cliente, Long> {
    Direccion findDireccionById(Long id);
}

package duoc.amaru.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.usuarios.model.Cliente;

public interface ClienteRepo extends JpaRepository<Cliente, Long> {
    
}

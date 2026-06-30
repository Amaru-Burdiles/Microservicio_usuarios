package duoc.amaru.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.usuarios.model.Direccion;

public interface DireccionRepo extends JpaRepository<Direccion, Long> {
    
}

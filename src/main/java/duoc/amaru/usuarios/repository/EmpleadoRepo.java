package duoc.amaru.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.usuarios.model.Empleado;

public interface EmpleadoRepo extends JpaRepository<Empleado, Long> {
    
}

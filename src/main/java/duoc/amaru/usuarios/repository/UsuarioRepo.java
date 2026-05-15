package duoc.amaru.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.usuarios.model.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    
}

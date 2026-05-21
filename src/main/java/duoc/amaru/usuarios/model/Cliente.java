package duoc.amaru.usuarios.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Cliente extends Usuario {

    @OneToMany
    @JoinColumn(name = "cliente_id")
    private List<Direccion> derecciones;
}

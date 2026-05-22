package duoc.amaru.usuarios.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @OneToMany
    @JoinColumn(name = "cliente_id")
    private List<Direccion> direcciones;

    @Transient
    private final AtomicInteger tagId = new AtomicInteger(1);
}

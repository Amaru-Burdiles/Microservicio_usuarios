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

    public Cliente(Long id, String rut, String pNomb, String sNomb, String pApel, String sApel, String mail, String password, String telefono, String estado, int nvlPermiso, List<Direccion> direcciones) {
        super(id, rut, pNomb, sNomb, pApel, sApel, mail, password, telefono, estado, nvlPermiso);
        this.direcciones = direcciones;
    }
}

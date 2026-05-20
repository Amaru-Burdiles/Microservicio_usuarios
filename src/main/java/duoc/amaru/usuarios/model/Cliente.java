package duoc.amaru.usuarios.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    // TODO: Conectar las tablas tarjeta y direccion
    private List<Tarjeta> tarjetas;
    private List<Direccion> derecciones;

    // REGISTRO DE CLIENTE (COMPLETO)
    public Cliente(String rut, String dv, String pNomb, String sNomb, String pApel, String sApel, String email, String password, String telefono) {
        super(rut, dv, pNomb, sNomb, pApel, sApel, email, password, telefono, 1);
    }

    // REGISTRO DE CLIENTE (SOLO CAMPOS OBLIGATORIOS)
    public Cliente(String rut, String dv, String pNomb, String pApel, String email, String password, String telefono) {
        super(rut, dv, pNomb, pApel, email, password, telefono, 1);
    }
}

package duoc.amaru.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut_user")
    @NotBlank
    @Size(max = 11, min = 9)
    private String rut;

    @Column(name = "dv_user")
    @NotBlank
    @Size(max = 1, min = 1)
    private String dv;

    @Column(name = "p_nombre_user")
    @NotBlank
    @Size(max = 30)
    private String pNombre;

    @Column(name = "s_nombre_user")
    @Size(max = 30)
    private String sNombre;

    @Column(name = "p_apellido_user")
    @NotBlank
    @Size(max = 30)
    private String pApellido;

    @Column(name = "s_apellido_user")
    @Size(max = 30)
    private String sApellido;

    @Column(name = "correo_user")
    @NotBlank
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+"
                    + "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)"
                    + "*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+"
                    + "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
             message = "Correo inválido")
    private String correo;

    @Column(name = "password_user")
    @NotBlank
    @Size(min = 8)
    private String password;

    @Column(name = "telefono_user")
    @NotBlank
    @Pattern(regexp = "^9? ?(\\d{4}) ?(\\d{4})$", message = "Número de telefono inválido")
    private String telefono;

    @Column(name = "estado_user")
    private String estado;

    //Constructor
    public Usuario(String rut, String dv, String pNomb, String sNomb, String pApel, String sApel, String email, String password, String telefono) {
        this.rut = rut;
        this.dv = dv;
        this.pNombre = pNomb;
        this.sNombre = sNomb;
        this.pApellido = pApel;
        this.sApellido = sApel;
        this.correo = email;
        this.password = password;
        this.telefono = telefono;
        this.estado = "activo";
    }
}

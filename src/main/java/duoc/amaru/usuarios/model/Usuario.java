package duoc.amaru.usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut_user")
    @NotBlank
    @Size(max = 13, min = 10)
    @Pattern(regexp = "^((\\d{2})(\\d{3})(\\d{3})-([0-9kK]))$"
                    + "|"
                    + "^((\\d{2})\\.(\\d{3})\\.(\\d{3}))-([0-9kK])$",
            message = "Rut inválido. Ejemplos válidos: 12.345.678-9; 12345678-9")
    private String rut;

    @Column(name = "p_nombre_user")
    @NotBlank(message = "El primer nombre no puede estar vacío")
    @Size(max = 30)
    private String pNombre;

    @Column(name = "s_nombre_user")
    @Size(max = 30)
    private String sNombre;

    @Column(name = "a_paterno_user")
    @NotBlank(message = "El apellido paterno no puede estar vacío")
    @Size(max = 30)
    private String aPaterno;

    @Column(name = "a_materno_user")
    @Size(max = 30)
    private String aMaterno;

    @Column(name = "correo_user", unique = true)
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String correo;

    @Column(name = "password_user")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String password;

    @Column(name = "telefono_user")
    @NotBlank(message = "El número de telefono es obligatorio")
    @Pattern(regexp = "^9? ?(\\d{4}) ?(\\d{4})$", message = "Número de telefono inválido")
    private String telefono;

    @Column(name = "estado_user")
    private String estado;

    @Column(nullable = true, name = "nvl_permiso_user")
    private int nvlPermiso;
}

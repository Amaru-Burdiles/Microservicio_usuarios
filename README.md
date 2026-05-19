# Microservicio_usuarios

Micro servicio de usuarios

## Testeo

### Registrar cliente

Para el testeo del registro de un cliente en el sistema, utilizamos el siguiente **JSON**:

```
{
    "rut": "",
    "dv": "",
    "pNombre": "",
    "pApellido": "",
    "correo": "",
    "password": "",
    "telefono": ""
}
```

Donde los espacios entre comillas (`""`) se reemplazan con los respectivos valores de testeo.

Es importante destacar algunas reglas de validación ya implementadas:

* rut
  * No puede estar vacío
  * Debe tener entre 11 y 9 caracteres
  * Puede o no llevar puntos

* dv
  * No puede estar vacío
  * Es solo un caracter

* pNombre
  * No puede estar vacío

* pApellido
  * No puede estar vacío

* correo
  * No puede estar vacío

* password
  * No puede estar vacía
  * Minimo 8 caracteres

* telefono
  * No puede estar vacío
  * Puede o no empezar con `9`
  * debe contener 2 grupos de **4 números**  
  que pueden o no estar separados por un espacio

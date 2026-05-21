# Microservicio_usuarios

Micro servicio de usuarios

## Testeo

### Registrar cliente

Para el testeo del registro de un cliente en el sistema, utilizamos el siguiente **JSON**:

```
{
    "rut": "",
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
  * Debe tener entre 13 y 10 caracteres
  * Puede o no llevar puntos

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

## Iniciar sesión
Para el testeo de inicio de sesión usamos
```
{
    "correo": "",
    "password": ""
}
```
El correo debe estar registrado y la contraseña debe coincidir con la del registro

# API de Mesa de Ayuda (Helpdesk) con SLA

API REST para gestión de tickets de soporte técnico, con autenticación JWT
(access token + refresh token) y autorización por roles (RBAC).

## Stack

- Java 17+
- Spring Boot 3.x
- Spring Security + JWT (jjwt)
- Spring Data JPA
- H2 Database (en memoria)
- BCrypt para contraseñas

## Cómo ejecutar

1. Clonar el repositorio.
2. Tener Java 17+ y Maven instalados.
3. Ejecutar: o desde el IDE, correr `HelpdeskApiApplication`.
4. La API queda disponible en `http://localhost:8080`.
5. Consola H2 (solo desarrollo): `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:helpdeskdb`
   - Usuario: `sa`
   - Password: (vacío)

## Configuración (application.properties)

- `jwt.secret`: clave usada para firmar los JWT.
- `jwt.access-token-expiration-ms`: 900000 (15 minutos).
- `jwt.refresh-token-expiration-ms`: 604800000 (7 días).

## Estrategia de refresh token: Opción A (persistido en base de datos)

Se eligió la Opción A porque permite revocar tokens de forma real en el logout
(marcando `revocado = true` en la tabla `refresh_tokens`), lo cual da control
explícito sobre el ciclo de vida de las sesiones y es más didáctico para
entender el flujo completo access token / refresh token / revocación,
en vez de depender de una denylist adicional como exigiría la Opción B (JWT
stateless).

## Modelo de roles

- `USUARIO`: puede crear tickets y ver solo los suyos.
- `SOPORTE`: puede ver todos los tickets, cambiar su estado y ver los vencidos.
- `ADMIN`: además de lo anterior, puede ascender usuarios a `SOPORTE`.

## Endpoints

### Públicos
| Método | Ruta | Descripción |
|---|---|---|
| GET | /api/ping | Verifica que la API está viva |
| POST | /api/auth/registro | Registra un usuario (rol USUARIO) |
| POST | /api/auth/login | Login, devuelve accessToken + refreshToken |
| POST | /api/auth/refresh | Renueva el accessToken |

### Autenticados
| Método | Ruta | Descripción |
|---|---|---|
| POST | /api/auth/logout | Revoca el refreshToken |
| POST | /api/tickets | Crea un ticket |
| GET | /api/tickets/mios | Lista tickets propios |
| GET | /api/tickets/{id} | Consulta un ticket (dueño o SOPORTE/ADMIN) |

### Por rol (SOPORTE / ADMIN)
| Método | Ruta | Descripción |
|---|---|---|
| GET | /api/tickets | Lista todos los tickets |
| PATCH | /api/tickets/{id}/estado | Cambia el estado de un ticket |
| GET | /api/tickets/vencidos | Lista tickets vencidos |

### Por rol (ADMIN)
| Método | Ruta | Descripción |
|---|---|---|
| POST | /api/admin/soporte | Asciende un usuario a SOPORTE |

## Pruebas

Se incluye la colección de Postman `helpdesk-api.postman_collection.json`
con el ambiente `helpdesk-api.postman_environment.json`. Importar ambos en
Postman y correr en orden: Ping → Registro → Login → ... (ver colección).

## Limitación conocida

No existe un usuario ADMIN semilla al arrancar la aplicación. Para probar
`POST /api/admin/soporte` o crear el primer SOPORTE, se debe cambiar el rol
manualmente vía la consola H2:

```sql
UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'algun-usuario@correo.com';
```

Luego hacer login de nuevo con ese usuario para obtener un token con el rol
actualizado (el rol queda grabado dentro del JWT en el momento del login).
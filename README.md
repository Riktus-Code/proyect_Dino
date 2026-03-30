# Project Dino - Monorepo Boda

Estructura inicial de un monorepo Nx con:

- Frontend Angular: apps/wedding-web
- Backend Spring Boot (Maven): apps/wedding-api

## Requisitos

- Node.js 20+ (recomendado)
- npm
- Java 17+
- Maven 3.9+

## Instalar dependencias

```bash
npm install
```

## Arrancar frontend (Angular)

```bash
npx nx serve wedding-web
```

Disponible en: http://localhost:4200

## Arrancar backend (Spring Boot)

Opcion 1 (via Nx target):

```bash
npx nx serve wedding-api
```

Opcion 2 (Maven directo):

```bash
cd apps/wedding-api
mvn spring-boot:run
```

Disponible en: http://localhost:8080

## Scripts npm utiles

- npm run start:front
- npm run start:back
- npm run start:back:jdk25

## Guia rapida de trabajo (persistente)

Esta seccion sirve para futuras consultas del proyecto.

- Frontend siempre en puerto 4200:

```powershell
Set-Location "C:\Users\elman\Desktop\Project Dino"; npx nx serve wedding-web --port=4200
```

- Backend con JDK 25 (recomendado):

```powershell
Set-Location "C:\Users\elman\Desktop\Project Dino"; npm run start:back:jdk25
```

- Parar servidores en la terminal activa:

```text
Ctrl + C
```

- Parar procesos si se quedan colgados (Angular/API):

```powershell
$ports = 4200,8080
foreach ($p in $ports) {
	$conns = Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue
	if ($conns) {
		$ids = $conns | Select-Object -ExpandProperty OwningProcess -Unique
		foreach ($id in $ids) { Stop-Process -Id $id -Force }
	}
}
```

- Comprobar puertos libres:

```powershell
$ports = 4200,8080
foreach ($p in $ports) {
	$conns = Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue
	if ($conns) { "PORT $p ocupado" } else { "PORT $p libre" }
}
```

## Estructura principal

- apps/wedding-web
- apps/wedding-api

## Rutas frontend actuales

- /
- /invitacion
- /detalles
- /confirmacion

## Endpoints backend actuales

- GET /api/detalles-boda
- POST /api/confirmacion
- GET /api/usuarios
- GET /api/usuarios/{id}
- POST /api/usuarios
- PUT /api/usuarios/{id}
- DELETE /api/usuarios/{id}

CORS habilitado para origen: http://localhost:4200. En la API de usuarios se permiten GET, POST, PUT, DELETE y OPTIONS.

## Avance de implementacion (30-03-2026)

- Se creo/ajusto la entidad JPA `Usuario` en `apps/wedding-api/src/main/java/com/wedding/api/entity/Usuario.java`.
- La tabla asociada es `usuario`.
- Campos definidos:
	- `id` (Primary Key, autoincremental con `GenerationType.IDENTITY`)
	- `nombre` (`NOT NULL`)
	- `apellido` (`NOT NULL`)
	- `correo` (`NOT NULL`, `UNIQUE`)
- Con `spring.jpa.hibernate.ddl-auto=update` en `application.properties`, Spring Boot crea o actualiza la tabla automaticamente al iniciar.

## Avance de implementacion (30-03-2026) - Orden 2

- Se implemento CRUD completo para usuarios en `apps/wedding-api/src/main/java/com/wedding/api/controller/UsuarioController.java`.
- Endpoints habilitados: listar, obtener por id, crear, actualizar y eliminar.
- CORS para usuarios habilitado desde `http://localhost:4200` con metodos GET, POST, PUT, DELETE y OPTIONS.

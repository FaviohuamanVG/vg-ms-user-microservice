# 🏛️ VG Users Microservice

## 📋 Tabla de Contenidos
- [Contexto](#contexto)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías](#tecnologías)
- [Funcionalidades](#funcionalidades)
- [Endpoints API](#endpoints-api)
- [Instalación y Configuración](#instalación-y-configuración)
- [Ejemplos de Uso](#ejemplos-de-uso)

---

## 🎯 Contexto

El **VG Users Microservice** es un sistema de gestión integral de usuarios diseñado para instituciones educativas del grupo Valle Grande. Este microservicio maneja la autenticación, autorización y gestión de usuarios, así como sus asignaciones a diferentes instituciones educativas.

### Problema que Resuelve
- **Gestión centralizada** de usuarios en múltiples sedes
- **Asignación flexible** de usuarios a diferentes instituciones con roles específicos
- **Trazabilidad completa** de cambios y movimientos de usuarios
- **Integración** con Keycloak para autenticación y autorización
- **Eliminación lógica y física** de registros

---

## 🏗️ Arquitectura

### Arquitectura Hexagonal (Clean Architecture)
```
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                     │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │      REST       │  │   REPOSITORY    │  │     CONFIG      │ │
│  │   Controllers   │  │   (MongoDB)     │  │   (CORS, etc)   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                        │
│  ┌─────────────────┐  ┌─────────────────┐                   │
│  │    SERVICES     │  │   INTERFACES    │                   │
│  │ (Business Logic)│  │  (Contracts)    │                   │
│  └─────────────────┘  └─────────────────┘                   │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │     MODELS      │  │      ENUMS      │  │      DTOs       │ │
│  │   (Entities)    │  │  (UserStatus,   │  │  (Requests,     │ │
│  │                 │  │   Roles, etc)   │  │   Responses)    │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Tecnologías Utilizadas
- **Framework:** Spring Boot 3.x
- **Reactive Programming:** Spring WebFlux (Reactor)
- **Base de Datos:** MongoDB (Reactive)
- **Autenticación:** Keycloak Integration
- **Documentación:** Swagger/OpenAPI
- **Build Tool:** Maven
- **Java Version:** 17+

---

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/pe/edu/vallegrande/vgmsuser/
│   │   ├── VgMsUserApplication.java                 # Main Application
│   │   ├── application/                             # APPLICATION LAYER
│   │   │   ├── impl/                               # Service Implementations
│   │   │   │   ├── AdminUserServiceImpl.java
│   │   │   │   ├── AuthServiceImpl.java
│   │   │   │   ├── EmailServiceImpl.java
│   │   │   │   ├── KeycloakServiceImpl.java
│   │   │   │   ├── UserManagementServiceImpl.java
│   │   │   │   └── UserInstitutionServiceImpl.java  # NEW: Institution Management
│   │   │   └── service/                            # Service Interfaces
│   │   │       ├── IAdminUserService.java
│   │   │       ├── IAuthService.java
│   │   │       ├── IEmailService.java
│   │   │       ├── IKeycloakService.java
│   │   │       ├── IUserManagementService.java
│   │   │       └── IUserInstitutionService.java     # NEW: Institution Service
│   │   ├── domain/                                  # DOMAIN LAYER
│   │   │   └── model/                              # Domain Models
│   │   │       ├── User.java                       # User Entity
│   │   │       ├── UserProfile.java                # User Profile Entity
│   │   │       ├── UserInstitutionRelation.java    # NEW: Institution Relations
│   │   │       ├── InstitutionAssignment.java      # NEW: Assignment Entity
│   │   │       ├── AssignmentMovement.java         # NEW: Movement Tracking
│   │   │       ├── dto/                            # Data Transfer Objects
│   │   │       │   ├── AssignInstitutionRequest.java
│   │   │       │   └── UpdateRoleRequest.java
│   │   │       └── enums/                          # Domain Enums
│   │   │           ├── UserStatus.java
│   │   │           ├── DocumentType.java
│   │   │           ├── PasswordStatus.java
│   │   │           ├── InstitutionRole.java        # NEW: Institution Roles
│   │   │           ├── AssignmentStatus.java       # NEW: Assignment Status
│   │   │           └── AssignmentAction.java       # NEW: Action Types
│   │   └── infrastructure/                         # INFRASTRUCTURE LAYER
│   │       ├── config/                            # Configuration
│   │       │   ├── CorsConfig.java
│   │       │   └── MongoConfig.java
│   │       ├── repository/                        # Data Repositories
│   │       │   ├── UserProfileRepository.java
│   │       │   └── UserInstitutionRelationRepository.java  # NEW
│   │       ├── rest/                              # REST Controllers
│   │       │   ├── UserManagementRest.java
│   │       │   └── UserInstitutionRest.java       # NEW: Institution Endpoints
│   │       └── util/                              # Utilities
│   └── resources/
│       ├── application.yml                        # Configuration
│       └── templates/                            # Email Templates
│           ├── reset-password-form.html
│           └── email/
│               ├── password-change-confirmation.html
│               ├── password-reset.html
│               └── temporary-credentials.html
└── test/                                         # Test Classes
    └── java/pe/edu/vallegrande/vgmsuser/
        └── VgMsUserApplicationTests.java
```

---

## ⚡ Funcionalidades

### 👥 Gestión de Usuarios
- ✅ **Crear usuarios completos** (Keycloak + MongoDB)
- ✅ **Autenticación y autorización** integrada con Keycloak
- ✅ **Gestión de perfiles** de usuario
- ✅ **Cambio de estados** (ACTIVE, INACTIVE, SUSPENDED)
- ✅ **Reset de contraseñas** con tokens
- ✅ **Envío de emails** automáticos
- ✅ **Eliminación lógica y física**

### 🏢 Gestión de Instituciones (NUEVO)
- ✅ **Asignación múltiple** de usuarios a instituciones
- ✅ **Roles diferenciados** por institución
- ✅ **Trazabilidad completa** de movimientos
- ✅ **Historial de cambios** (roles, estados, asignaciones)
- ✅ **Activación/Desactivación** de asignaciones
- ✅ **Consultas avanzadas** por usuario e institución

### 📊 Auditoria y Seguimiento
- ✅ **Registro de movimientos** con timestamps
- ✅ **Descripción detallada** de cada acción
- ✅ **Estados anteriores y nuevos** en cada cambio
- ✅ **Logs estructurados** para debugging

---

## 🔗 Endpoints API

### 👤 User Management (`/api/v1/user-director`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/users` | Crear usuario completo |
| `GET` | `/users` | Obtener todos los usuarios |
| `GET` | `/users/keycloak/{keycloakId}` | Obtener usuario por Keycloak ID |
| `GET` | `/users/username/{username}` | Obtener usuario por username |
| `PUT` | `/users/{keycloakId}` | Actualizar usuario |
| `DELETE` | `/users/{keycloakId}` | Eliminar usuario |
| `PATCH` | `/users/{keycloakId}/status` | Cambiar estado |
| `PATCH` | `/users/{keycloakId}/activate` | Activar usuario |
| `PATCH` | `/users/{keycloakId}/deactivate` | Desactivar usuario |
| `GET` | `/users/status/{status}` | Usuarios por estado |

### 🏢 Institution Management (`/api/v1/user-institution`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | **Obtener todas las relaciones** |
| `POST` | `/users/{userId}/assign-institution` | **Asignar usuario a institución** |
| `GET` | `/users/by-institution/{institutionId}` | **Usuarios por institución** |
| `PUT` | `/users/{userId}/institutions/{institutionId}/roles` | **Actualizar rol** |
| `PATCH` | `/users/{userId}/institutions/{institutionId}/deactivate` | **Eliminación lógica** |
| `PATCH` | `/users/{userId}/institutions/{institutionId}/activate` | **Restaurar asignación** |
| `DELETE` | `/users/{userId}/institutions/all` | **Eliminación física completa** |
| `DELETE` | `/users/{userId}/institutions/{institutionId}` | **Eliminar asignación específica** |
| `GET` | `/users/{userId}/institutions/{institutionId}` | **Relación específica** |
| `GET` | `/users/{userId}/institutions` | **Relaciones de usuario** |
| `GET` | `/users/{userId}/institutions/exists` | **Verificar si existen relaciones** |
| `PATCH` | `/users/{userId}/institutions/status` | **Cambiar estado general** |
| `PATCH` | `/users/{userId}/institutions/deactivate-all` | **Desactivar TODAS las asignaciones** |
| `PATCH` | `/users/{userId}/institutions/activate-all` | **Activar TODAS las asignaciones** |

---

## 📝 Ejemplos de Uso

### 🆕 Crear Asignación de Usuario a Institución

**Request:**
```http
POST /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/assign-institution
Content-Type: application/json

{
  "institutionId": "inst-vallegrande-001",
  "role": "TEACHER",
  "assignmentDate": "2025-09-07T08:00:00",
  "endDate": "2025-12-31T17:00:00",
  "description": "Profesor de Matemáticas - Nivel Secundaria"
}
```

**Response (201 Created):**
```json
{
  "id": "68bdf9ae7a49cbfc0dcea60d",
  "userId": "68b8eb9c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "TEACHER",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T16:31:26.207",
          "action": "ASSIGNED",
          "newRole": "TEACHER",
          "newStatus": "ACTIVE",
          "description": "Profesor de Matemáticas - Nivel Secundaria"
        }
      ],
      "status": "ACTIVE"
    }
  ],
  "createdAt": "2025-09-07T16:31:26.207",
  "updatedAt": "2025-09-07T16:31:26.207",
  "status": "ACTIVE"
}
```

### ✏️ Actualizar Rol en Institución

**Comportamiento:** Al actualizar un rol, se mantiene la **misma asignación** pero se actualiza el rol y se registra el historial completo en los movimientos.

**Request:**
```http
PUT /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/institutions/inst-vallegrande-001/roles
Content-Type: application/json

{
  "newRole": "DIRECTOR",
  "description": "Promoción a director por excelente desempeño"
}
```

**Response (200 OK):**
```json
{
  "id": "68be12b292855ce38a140fa3",
  "userId": "68b080c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "DIRECTOR",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T18:18:28.860",
          "action": "STATUS_CHANGED",
          "oldRole": "TEACHER",
          "newRole": null,
          "oldStatus": "ACTIVE",
          "newStatus": "INACTIVE",
          "description": "Rol TEACHER terminado para cambio a DIRECTOR"
        },
        {
          "date": "2025-09-07T18:18:28.860",
          "action": "ROLE_CHANGED",
          "oldRole": "TEACHER",
          "newRole": "DIRECTOR",
          "oldStatus": "INACTIVE",
          "newStatus": "ACTIVE",
          "description": "Promoción a director por excelente desempeño"
        }
      ],
      "status": "ACTIVE"
    }
  ],
  "createdAt": "2025-09-07T18:18:09.897",
  "updatedAt": "2025-09-07T18:18:28.861",
  "status": "ACTIVE"
}
```

### ❌ Desactivar Asignación (Eliminación Lógica)

**Comportamiento:** Solo cambia el estado de `ACTIVE` a `INACTIVE` sin agregar movimientos al historial.

**Request:**
```http
PATCH /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/institutions/inst-vallegrande-001/deactivate
```

**Response (200 OK):**
```json
{
  "id": "68be12b292855ce38a140fa3",
  "userId": "68b080c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "TEACHER",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T18:18:10.013",
          "action": "ASSIGNED",
          "newRole": "TEACHER",
          "newStatus": "ACTIVE",
          "description": "Asignación inicial como profesor"
        }
      ],
      "status": "INACTIVE"
    }
  ],
  "createdAt": "2025-09-07T18:18:09.897",
  "updatedAt": "2025-09-07T18:34:27.139",
  "status": "ACTIVE"
}
```

### ✅ Activar Asignación (Restaurar)

**Comportamiento:** Solo cambia el estado de `INACTIVE` a `ACTIVE` sin agregar movimientos al historial.

**Request:**
```http
PATCH /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/institutions/inst-vallegrande-001/activate
```

**Response (200 OK):**
```json
{
  "id": "68be12b292855ce38a140fa3",
  "userId": "68b080c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "TEACHER",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T18:18:10.013",
          "action": "ASSIGNED",
          "newRole": "TEACHER",
          "newStatus": "ACTIVE",
          "description": "Asignación inicial como profesor"
        }
      ],
      "status": "ACTIVE"
    }
  ],
  "createdAt": "2025-09-07T18:18:09.897",
  "updatedAt": "2025-09-07T18:35:15.245",
  "status": "ACTIVE"
}
```

---

## 🎯 Roles y Estados

### 👔 Roles Disponibles (`InstitutionRole`)
- `TEACHER` - Profesor
- `DIRECTOR` - Director
- `AUXILIARY` - Auxiliar
- `SECRETARY` - Secretario
- `COORDINATOR` - Coordinador
- `ADMINISTRATIVE` - Administrativo
- `STUDENT` - Estudiante
- `VISITOR` - Visitante

### 📊 Estados de Usuario (`UserStatus`)
- `ACTIVE` - Usuario activo
- `INACTIVE` - Usuario inactivo
- `SUSPENDED` - Usuario suspendido
- `TERMINATED` - Usuario terminado

### 🏢 Estados de Asignación (`AssignmentStatus`)
- `ACTIVE` - Asignación activa
- `INACTIVE` - Asignación inactiva
- `SUSPENDED` - Asignación suspendida
- `TERMINATED` - Asignación terminada

### 📋 Acciones de Movimiento (`AssignmentAction`)
- `ASSIGNED` - Usuario asignado
- `ROLE_CHANGED` - Cambio de rol
- `STATUS_CHANGED` - Cambio de estado
- `TERMINATED` - Terminación
- `REACTIVATED` - Reactivación
- `MODIFIED` - Modificación general

---

### 🔄 Desactivar TODAS las Asignaciones de Usuario

**Comportamiento:** Desactiva todas las asignaciones de instituciones de un usuario y agrega movimientos de desactivación masiva para cada asignación que cambie de estado.

**Request:**
```http
PATCH /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/institutions/deactivate-all
```

**Response (200 OK):**
```json
{
  "id": "68bdf9ae7a49cbfc0dcea60d",
  "userId": "68b8eb9c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "TEACHER",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T16:31:26.207",
          "action": "ASSIGNED",
          "newRole": "TEACHER",
          "newStatus": "ACTIVE",
          "description": "Profesor de Matemáticas - Nivel Secundaria"
        },
        {
          "date": "2025-09-07T17:00:15.125",
          "type": "STATUS_CHANGE",
          "description": "Desactivación masiva de todas las asignaciones",
          "previousStatus": "ACTIVE",
          "newStatus": "INACTIVE"
        }
      ],
      "status": "INACTIVE",
      "statusChangedDate": "2025-09-07T17:00:15.125"
    },
    {
      "institutionId": "inst-vallegrande-002",
      "role": "ADMIN",
      "assignmentDate": "2025-08-01T08:00:00.000",
      "movements": [
        {
          "date": "2025-08-01T10:30:00.000",
          "action": "ASSIGNED",
          "newRole": "ADMIN",
          "newStatus": "ACTIVE",
          "description": "Administrador de sede principal"
        },
        {
          "date": "2025-09-07T17:00:15.125",
          "type": "STATUS_CHANGE", 
          "description": "Desactivación masiva de todas las asignaciones",
          "previousStatus": "ACTIVE",
          "newStatus": "INACTIVE"
        }
      ],
      "status": "INACTIVE",
      "statusChangedDate": "2025-09-07T17:00:15.125"
    }
  ],
  "createdAt": "2025-09-07T16:31:26.207",
  "updatedAt": "2025-09-07T17:00:15.125",
  "status": "ACTIVE"
}
```

### ✅ Activar TODAS las Asignaciones de Usuario

**Comportamiento:** Activa todas las asignaciones de instituciones de un usuario y agrega movimientos de activación masiva para cada asignación que cambie de estado.

**Request:**
```http
PATCH /api/v1/user-institution/users/68b8eb9c3bd340fcaa9ca81fb/institutions/activate-all
```

**Response (200 OK):**
```json
{
  "id": "68bdf9ae7a49cbfc0dcea60d",
  "userId": "68b8eb9c3bd340fcaa9ca81fb",
  "institutionAssignments": [
    {
      "institutionId": "inst-vallegrande-001",
      "role": "TEACHER",
      "assignmentDate": "2025-09-07T08:00:00.000",
      "endDate": "2025-12-31T17:00:00.000",
      "movements": [
        {
          "date": "2025-09-07T16:31:26.207",
          "action": "ASSIGNED",
          "newRole": "TEACHER",
          "newStatus": "ACTIVE",
          "description": "Profesor de Matemáticas - Nivel Secundaria"
        },
        {
          "date": "2025-09-07T17:00:15.125",
          "type": "STATUS_CHANGE",
          "description": "Desactivación masiva de todas las asignaciones",
          "previousStatus": "ACTIVE",
          "newStatus": "INACTIVE"
        },
        {
          "date": "2025-09-07T17:05:30.456",
          "type": "STATUS_CHANGE",
          "description": "Activación masiva de todas las asignaciones",
          "previousStatus": "INACTIVE",
          "newStatus": "ACTIVE"
        }
      ],
      "status": "ACTIVE",
      "statusChangedDate": "2025-09-07T17:05:30.456"
    },
    {
      "institutionId": "inst-vallegrande-002",
      "role": "ADMIN",
      "assignmentDate": "2025-08-01T08:00:00.000",
      "movements": [
        {
          "date": "2025-08-01T10:30:00.000",
          "action": "ASSIGNED",
          "newRole": "ADMIN",
          "newStatus": "ACTIVE",
          "description": "Administrador de sede principal"
        },
        {
          "date": "2025-09-07T17:00:15.125",
          "type": "STATUS_CHANGE",
          "description": "Desactivación masiva de todas las asignaciones",
          "previousStatus": "ACTIVE",
          "newStatus": "INACTIVE"
        },
        {
          "date": "2025-09-07T17:05:30.456",
          "type": "STATUS_CHANGE",
          "description": "Activación masiva de todas las asignaciones",
          "previousStatus": "INACTIVE",
          "newStatus": "ACTIVE"
        }
      ],
      "status": "ACTIVE",
      "statusChangedDate": "2025-09-07T17:05:30.456"
    }
  ],
  "createdAt": "2025-09-07T16:31:26.207",
  "updatedAt": "2025-09-07T17:05:30.456",
  "status": "ACTIVE"
}
```

---

## 🚀 Instalación y Configuración

### Prerrequisitos
- Java 17+
- Maven 3.8+
- MongoDB 4.4+
- Keycloak Server

### Configuración

1. **Clonar el repositorio:**
```bash
git clone <repository-url>
cd vg-users-microservice
```

2. **Configurar MongoDB y Keycloak en `application.yml`:**
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/vg_users_db
      
keycloak:
  server-url: http://localhost:8080
  realm: vg-realm
  client-id: vg-users-client
```

3. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

4. **La aplicación estará disponible en:**
```
http://localhost:8100
```

---

## 📚 Documentación Adicional

### Swagger/OpenAPI
- **URL:** `http://localhost:8100/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8100/v3/api-docs`

### Logs
- Los logs incluyen información detallada de cada operación
- Nivel configurable en `application.yml`
- Trazabilidad completa de movimientos de usuarios

---

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver `LICENSE` para más detalles.

---

## 👥 Autores

- **Valle Grande Team** - *Desarrollo inicial* - [Valle Grande](https://vallegrande.edu.pe)

---

**🎓 Valle Grande - Formando Líderes del Futuro**
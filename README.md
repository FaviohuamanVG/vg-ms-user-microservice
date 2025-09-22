# Microservicio Academic - Course Management

## Descripción
Microservicio para la gestión de cursos académicos implementado con Spring Boot y MongoDB.

## Arquitectura
El proyecto sigue una arquitectura hexagonal con las siguientes capas:
- **Domain**: Contiene las entidades, DTOs y enums
- **Application**: Contiene los servicios y la lógica de negocio
- **Infrastructure**: Contiene los repositorios y controladores REST

## Tecnologías
- Java 17
- Spring Boot 3.5.5
- Spring WebFlux (Reactivo)
- Spring Data MongoDB Reactive
- Maven
- MongoDB

## Configuración

### Base de datos
El microservicio está configurado para usar MongoDB en:
- **Host**: localhost
- **Puerto**: 27017
- **Base de datos**: academic_db

### Puerto del servidor
El microservicio se ejecuta en el puerto **8103**

## Endpoints de la API

### Courses - Base URL: `http://localhost:8103/api/v1/courses`

### 1. Crear curso
- **POST** `/api/v1/courses`
- **Body**:
```json
{
    "institutionId": "inst-001",
    "courseCode": "COM-PRI",
    "courseName": "Comunicación",
    "level": "Primaria",
    "description": "habla, habla, habla",
    "hoursPerWeek": 5,
    "status": "A"
}
```

### 2. Obtener todos los cursos
- **GET** `/api/v1/courses`

### 3. Obtener curso por ID
- **GET** `/api/v1/courses/{id}`

### 4. Obtener cursos por institución
- **GET** `/api/v1/courses/by-institution/{institutionId}`

### 5. Obtener cursos por institución y nivel
- **GET** `/api/v1/courses/by-institution/{institutionId}/level/{level}`

### 6. Obtener cursos por estado
- **GET** `/api/v1/courses/status/{status}`
- **Valores válidos para status**: `A`, `I`

### 7. Actualizar curso
- **PUT** `/api/v1/courses/{id}`
- **Body**: Mismo formato que crear curso

### 8. Eliminado lógico de curso
- **PATCH** `/api/v1/courses/{id}/delete`

### 9. Restaurar curso
- **PATCH** `/api/v1/courses/{id}/restore`

### 10. Verificar si existe un código de curso
- **GET** `/api/v1/courses/exists/{courseCode}`

---

### Periods - Base URL: `http://localhost:8103/api/v1/periods`

### 1. Crear período académico
- **POST** `/api/v1/periods`
- **Body**:
```json
{
    "institutionId": "inst-001",
    "level": "Primaria",
    "period": "1-5 para secundaria",
    "academicYear": "2025",
    "periodType": "SEMESTER",
    "startDate": "2025-03-01",
    "endDate": "2025-07-31",
    "status": "A"
}
```

### 2. Obtener todos los períodos
- **GET** `/api/v1/periods`

### 3. Obtener período por ID
- **GET** `/api/v1/periods/{id}`

### 4. Obtener períodos por institución
- **GET** `/api/v1/periods/by-institution/{institutionId}`

### 5. Obtener períodos por institución y nivel
- **GET** `/api/v1/periods/by-institution/{institutionId}/level/{level}`

### 6. Obtener períodos por año académico
- **GET** `/api/v1/periods/academic-year/{academicYear}`

### 7. Obtener períodos por tipo
- **GET** `/api/v1/periods/type/{periodType}`
- **Valores válidos**: `ANNUAL`, `SEMESTER`, `TRIMESTER`, `BIMESTER`

### 8. Obtener períodos por estado
- **GET** `/api/v1/periods/status/{status}`
- **Valores válidos para status**: `A`, `I`

### 9. Actualizar período
- **PUT** `/api/v1/periods/{id}`
- **Body**: Mismo formato que crear período

### 10. Eliminado lógico de período
- **PATCH** `/api/v1/periods/{id}/delete`

### 11. Restaurar período
- **PATCH** `/api/v1/periods/{id}/restore`

### 12. Verificar si existe un período
- **GET** `/api/v1/periods/exists?institutionId={}&level={}&period={}&academicYear={}`

## Respuestas de la API

### Respuesta exitosa:
```json
{
    "success": true,
    "message": "Mensaje descriptivo",
    "data": { ... },
    "total": 10
}
```

### Respuesta de error:
```json
{
    "success": false,
    "message": "Mensaje de error",
    "errors": { ... }
}
```

## Estructura del proyecto
```
src/
├── main/
│   ├── java/vallegrande/edu/pe/vgmsacademic/
│   │   ├── AcademicApplication.java
│   │   ├── application/
│   │   │   ├── impl/CourseServiceImpl.java
│   │   │   └── service/CourseService.java
│   │   ├── domain/
│   │   │   └── model/
│   │   │       ├── Course.java
│   │   │       ├── dto/
│   │   │       │   ├── CourseRequestDto.java
│   │   │       │   └── CourseResponseDto.java
│   │   │       └── enums/StatusEnum.java
│   │   └── infraestructure/
│   │       ├── repository/CourseRepository.java
│   │       ├── rest/CourseController.java
│   │       └── util/GlobalExceptionHandler.java
│   └── resources/
│       └── application.yml
```

## Ejecución

### Prerrequisitos
1. Java 17
2. Maven 3.6+
3. MongoDB 4.4+

### Comandos
```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

## Validaciones

### Course
- `institutionId`: Obligatorio
- `courseCode`: Obligatorio y único
- `courseName`: Obligatorio
- `level`: Obligatorio
- `hoursPerWeek`: Obligatorio y mayor a 0
- `status`: Por defecto 'A' (A=Activo, I=Inactivo)

### Period
- `institutionId`: Obligatorio
- `level`: Obligatorio
- `period`: Obligatorio
- `academicYear`: Obligatorio
- `periodType`: Obligatorio (ANNUAL, SEMESTER, TRIMESTER, BIMESTER)
- `startDate`: Obligatorio
- `endDate`: Obligatorio y posterior a startDate
- `status`: Por defecto 'A' (A=Activo, I=Inactivo)
- Combinación única: institutionId + level + period + academicYear

## Funcionalidades implementadas
- ✅ CRUD completo de cursos y períodos académicos
- ✅ Programación reactiva con Mono/Flux (Spring WebFlux)
- ✅ Validación de datos de entrada con Bean Validation
- ✅ Manejo de excepciones personalizado
- ✅ Eliminado lógico con estados 'A'/'I'
- ✅ Restauración de registros eliminados
- ✅ Verificación de duplicados y reglas de negocio
- ✅ Filtrado por institución, nivel, año académico, tipo y estado
- ✅ Timestamps automáticos (createdAt, updatedAt)
- ✅ Respuestas estructuradas con status y mensajes descriptivos
- ✅ Validación de fechas en períodos académicos
 “Sin embargo, en lugar de empezar vendiendo algunos de nuestros artículos de mayor valor, él quiere que comencemos vendiendo solo libros (cambio planificado). Algunos artículos de nuestro catálogo cuestan más de 10.000 dólares y, hasta que sepamos que el sitio funciona bien y no se pierden pedidos, no queremos correr riesgos con artículos costosos. Pero si comprobamos que a nuestros clientes les gusta poder hacer pedidos en línea, y si hacemos un buen trabajo con el sitio, ampliaremos la oferta y venderemos el resto de nuestros productos en él”.

* “Un usuario puede realizar una búsqueda básica y sencilla que localice una palabra o frase tanto en el campo del autor como en el del título”.
* “Un usuario puede buscar libros introduciendo valores en cualquier combinación de autor, título e ISBN”.
* “Un usuario puede calificar los libros en una escala de 1 (malo) a 5 (bueno). No es necesario que el usuario haya comprado el libro a nosotros”.
* “Un usuario puede escribir una reseña sobre un libro. Puede previsualizar la reseña antes de enviarla. No es necesario que el usuario haya comprado el libro a nosotros”.

## Backend

### Cómo correrlo

1. Levantar la base de datos Postgres (una sola vez, queda corriendo entre reinicios):

```
docker compose up -d
```

2. Correr el backend:

```
cd backend
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8081`. La base de datos y sus credenciales están definidas en `docker-compose.yml`, por lo que no hace falta instalar Postgres manualmente.

### Documentación de la API

La API está documentada con OpenAPI/Swagger. Con el backend corriendo:

* Swagger UI (interactivo, para probar los endpoints): `http://localhost:8081/swagger-ui.html`
* Spec OpenAPI en JSON (para generar un cliente tipado en el frontend): `http://localhost:8081/v3/api-docs`

Esta es la fuente de verdad del contrato entre el backend y el frontend en React: cualquier endpoint nuevo o cambio de forma en un DTO se refleja ahí automáticamente.

### CORS

Los orígenes permitidos para el frontend se configuran en `application.properties` con `app.cors.allowed-origins` (por defecto `http://localhost:5173` y `http://localhost:3000`, los puertos default de Vite y Create React App).

### Manejo de errores

Los errores se devuelven en un formato JSON consistente:

```json
{
  "timestamp": "2026-09-20T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Libro no encontrado con id: 99",
  "path": "/api/libros/99"
}
```

Los errores de validación (`@Valid`) además incluyen un campo `fieldErrors` con el detalle por campo.

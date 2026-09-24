# Frontend React — Librería Virtual

Frontend hecho con React + Vite para el backend del proyecto Caso-Libreria-virtual.

## Requisitos

- Node.js 20 o superior recomendado
- Backend Spring Boot ejecutándose en `http://localhost:8081`

## Instalación

```bash
npm install
```

## Ejecutar

```bash
npm run dev
```

Después abre:

http://localhost:5173

## Backend

Por defecto el frontend consume:

http://localhost:8081/api

Si necesitas cambiarlo, crea `.env` a partir de `.env.example`:

```env
VITE_API_URL=http://localhost:8081/api
```

## Funcionalidades

- Listado de libros
- Búsqueda simple por título/autor
- Búsqueda avanzada por título, autor e ISBN
- Detalle de libro
- Promedio de calificaciones
- Calificación de 1 a 5 estrellas
- Listado de reseñas
- Previsualización de reseñas
- Publicación de reseñas
- Diseño responsive

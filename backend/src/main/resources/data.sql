-- Insertar datos de prueba para Libros
-- Sin id explicito: se deja que la columna IDENTITY lo genere.
-- ON CONFLICT evita que falle al reiniciar la app contra una base ya sembrada
-- (spring.sql.init.mode=always ejecuta este script en cada arranque).
INSERT INTO libro (titulo, autor, isbn, precio) VALUES
    ('Cien años de soledad', 'Gabriel García Márquez', '978-0307474728', 45000.0),
    ('Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8424115456', 60000.0)
ON CONFLICT (isbn) DO NOTHING;

-- Insertar datos de prueba para Calificaciones
-- Se busca el libro por isbn en vez de asumir el id (depende del autogenerado
-- por IDENTITY), y NOT EXISTS evita duplicar filas en cada reinicio.
INSERT INTO calificacion (valor, libro_id)
SELECT 5, l.id FROM libro l WHERE l.isbn = '978-0307474728'
AND NOT EXISTS (SELECT 1 FROM calificacion c WHERE c.libro_id = l.id AND c.valor = 5)
UNION ALL
SELECT 4, l.id FROM libro l WHERE l.isbn = '978-8424115456'
AND NOT EXISTS (SELECT 1 FROM calificacion c WHERE c.libro_id = l.id AND c.valor = 4)
UNION ALL
SELECT 5, l.id FROM libro l WHERE l.isbn = '978-8424115456'
AND NOT EXISTS (SELECT 1 FROM calificacion c WHERE c.libro_id = l.id AND c.valor = 5);

-- Insertar datos de prueba para Reseñas
INSERT INTO resena (nombre_usuario, texto, libro_id)
SELECT 'AnaPerez', 'Una obra maestra absoluta de la literatura latinoamericana.', l.id
FROM libro l WHERE l.isbn = '978-0307474728'
AND NOT EXISTS (SELECT 1 FROM resena r WHERE r.libro_id = l.id AND r.nombre_usuario = 'AnaPerez')
UNION ALL
SELECT 'CarlosLector', 'Muy entretenido en algunas partes, clásico indispensable.', l.id
FROM libro l WHERE l.isbn = '978-8424115456'
AND NOT EXISTS (SELECT 1 FROM resena r WHERE r.libro_id = l.id AND r.nombre_usuario = 'CarlosLector');
/*-- Insertar datos de prueba para Libros
INSERT INTO libro (id, titulo, autor, isbn, precio) VALUES
                                                        (1, 'Cien años de soledad', 'Gabriel García Márquez', '978-0307474728', 45000.0),
                                                        (2, 'Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8424115456', 60000.0);

-- Insertar datos de prueba para Calificaciones
INSERT INTO calificacion (valor, libro_id) VALUES
                                               (5, 1),
                                               (4, 2),
                                               (5, 2);

-- Insertar datos de prueba para Reseñas
INSERT INTO resena (nombre_usuario, texto, libro_id) VALUES
                                                         ('AnaPerez', 'Una obra maestra absoluta de la literatura latinoamericana.', 1),
                                                         ('CarlosLector', 'Muy entretenido en algunas partes, clásico indispensable.', 2);

 */
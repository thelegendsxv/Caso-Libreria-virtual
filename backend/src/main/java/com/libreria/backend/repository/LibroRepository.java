package com.libreria.backend.repository;

import com.libreria.backend.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Búsqueda simple: una palabra o frase en autor O título
    @Query("SELECT l FROM Libro l WHERE " +
            "LOWER(l.autor) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Libro> buscarPorAutorOTitulo(@Param("q") String q);

    // Búsqueda avanzada: cualquier combinación de autor, título e ISBN
    // El CAST es necesario porque Postgres no puede inferir el tipo de un
    // parametro NULL usado dentro de CONCAT, y falla con 500 si se omite
    // alguno de los tres parametros (justamente el caso de uso normal).
    @Query("SELECT l FROM Libro l WHERE " +
            "(:autor IS NULL OR LOWER(l.autor) LIKE LOWER(CONCAT('%', CAST(:autor AS string), '%'))) AND " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', CAST(:titulo AS string), '%'))) AND " +
            "(:isbn IS NULL OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', CAST(:isbn AS string), '%')))")
    List<Libro> busquedaAvanzada(@Param("autor") String autor,
                                 @Param("titulo") String titulo,
                                 @Param("isbn") String isbn);
}
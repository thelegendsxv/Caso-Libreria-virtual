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
    @Query("SELECT l FROM Libro l WHERE " +
            "(:autor IS NULL OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:isbn IS NULL OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :isbn, '%')))")
    List<Libro> busquedaAvanzada(@Param("autor") String autor,
                                 @Param("titulo") String titulo,
                                 @Param("isbn") String isbn);
}
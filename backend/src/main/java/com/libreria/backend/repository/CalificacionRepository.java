package com.libreria.backend.repository;

import com.libreria.backend.model.Calificacion;
import com.libreria.backend.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByLibroId(Long libroId);
}
package com.libreria.backend.service;

import com.libreria.backend.dto.CalificacionDTO;
import com.libreria.backend.exception.ResourceNotFoundException;
import com.libreria.backend.model.Calificacion;
import com.libreria.backend.model.Libro;
import com.libreria.backend.repository.CalificacionRepository;
import com.libreria.backend.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public CalificacionService(CalificacionRepository calificacionRepository,
                               LibroRepository libroRepository) {
        this.calificacionRepository = calificacionRepository;
        this.libroRepository = libroRepository;
    }

    // Requisito: calificar de 1 a 5, sin exigir que se haya comprado el libro
    public Calificacion calificar(Long libroId, CalificacionDTO dto) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + libroId));

        Calificacion calificacion = new Calificacion(dto.getValor(), libro);
        return calificacionRepository.save(calificacion);
    }

    public Double obtenerPromedio(Long libroId) {
        List<Calificacion> calificaciones = calificacionRepository.findByLibroId(libroId);
        if (calificaciones.isEmpty()) {
            return null;
        }
        return calificaciones.stream()
                .mapToInt(Calificacion::getValor)
                .average()
                .orElse(0.0);
    }
}

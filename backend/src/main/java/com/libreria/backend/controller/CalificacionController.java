package com.libreria.backend.controller;

import com.libreria.backend.dto.CalificacionDTO;
import com.libreria.backend.model.Calificacion;
import com.libreria.backend.service.CalificacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/libros/{libroId}/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    @Autowired
    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    // Requisito: calificar 1-5, sin exigir compra
    @PostMapping
    public ResponseEntity<Void> calificar(@PathVariable Long libroId,
                                          @Valid @RequestBody CalificacionDTO dto) {
        Calificacion calificacion = calificacionService.calificar(libroId, dto);
        URI location = URI.create("/api/libros/" + libroId + "/calificaciones/" + calificacion.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/promedio")
    public Double obtenerPromedio(@PathVariable Long libroId) {
        return calificacionService.obtenerPromedio(libroId);
    }
}

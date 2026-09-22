package com.libreria.backend.controller;

import com.libreria.backend.dto.ResenaDTO;
import com.libreria.backend.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/libros/{libroId}/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    @Autowired
    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    // Requisito: previsualizar SIN guardar
    @PostMapping("/previsualizar")
    public ResenaDTO previsualizar(@Valid @RequestBody ResenaDTO dto) {
        return resenaService.previsualizar(dto);
    }

    // Requisito: guardar la reseña, sin exigir compra
    @PostMapping
    public ResponseEntity<ResenaDTO> guardar(@PathVariable Long libroId, @Valid @RequestBody ResenaDTO dto) {
        ResenaDTO guardada = resenaService.guardar(libroId, dto);
        URI location = URI.create("/api/libros/" + libroId + "/resenas/" + guardada.getId());
        return ResponseEntity.created(location).body(guardada);
    }

    @GetMapping
    public List<ResenaDTO> listarPorLibro(@PathVariable Long libroId) {
        return resenaService.listarPorLibro(libroId);
    }
}

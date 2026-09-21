package com.libreria.backend.controller;

import com.libreria.backend.dto.LibroDTO;
import com.libreria.backend.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public List<LibroDTO> listarTodos() {
        return libroService.listarTodos();
    }

    @GetMapping("/{id}")
    public LibroDTO buscarPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id);
    }

    // Requisito: búsqueda simple en autor o título
    @GetMapping("/buscar")
    public List<LibroDTO> buscarSimple(@RequestParam String q) {
        return libroService.buscarSimple(q);
    }

    // Requisito: búsqueda avanzada por cualquier combinación
    @GetMapping("/busqueda-avanzada")
    public List<LibroDTO> busquedaAvanzada(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String isbn) {
        return libroService.busquedaAvanzada(autor, titulo, isbn);
    }
}
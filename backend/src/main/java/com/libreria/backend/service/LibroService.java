package com.libreria.backend.service;

import com.libreria.backend.dto.LibroDTO;
import com.libreria.backend.model.Libro;
import com.libreria.backend.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<LibroDTO> listarTodos() {
        return libroRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public LibroDTO buscarPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        return convertirADTO(libro);
    }

    // Requisito: búsqueda básica en autor o título
    public List<LibroDTO> buscarSimple(String q) {
        return libroRepository.buscarPorAutorOTitulo(q).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Requisito: búsqueda avanzada por cualquier combinación de autor, título e ISBN
    public List<LibroDTO> busquedaAvanzada(String autor, String titulo, String isbn) {
        return libroRepository.busquedaAvanzada(autor, titulo, isbn).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private LibroDTO convertirADTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getIsbn(),
                libro.getPrecio()
        );
    }
}

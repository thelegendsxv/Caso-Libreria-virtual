package com.libreria.backend.service;

import com.libreria.backend.dto.ResenaDTO;
import com.libreria.backend.exception.ResourceNotFoundException;
import com.libreria.backend.model.Libro;
import com.libreria.backend.model.Resena;
import com.libreria.backend.repository.LibroRepository;
import com.libreria.backend.repository.ResenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public ResenaService(ResenaRepository resenaRepository, LibroRepository libroRepository) {
        this.resenaRepository = resenaRepository;
        this.libroRepository = libroRepository;
    }

    // Requisito: previsualizar la reseña SIN guardarla
    public ResenaDTO previsualizar(ResenaDTO dto) {
        // Solo devuelve el mismo texto tal cual, no toca la base de datos
        return new ResenaDTO(null, dto.getNombreUsuario(), dto.getTexto());
    }

    // Requisito: enviar/guardar la reseña, sin exigir compra previa
    public ResenaDTO guardar(Long libroId, ResenaDTO dto) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + libroId));

        Resena resena = new Resena(dto.getNombreUsuario(), dto.getTexto(), libro);
        Resena guardada = resenaRepository.save(resena);

        return new ResenaDTO(guardada.getId(), guardada.getNombreUsuario(), guardada.getTexto());
    }

    public List<ResenaDTO> listarPorLibro(Long libroId) {
        return resenaRepository.findByLibroId(libroId).stream()
                .map(r -> new ResenaDTO(r.getId(), r.getNombreUsuario(), r.getTexto()))
                .collect(Collectors.toList());
    }
}
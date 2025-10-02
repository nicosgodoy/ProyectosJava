package com.grupod.docintelia.service;

import com.grupod.docintelia.model.Biblioteca;
import com.grupod.docintelia.repository.BibliotecaRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BibliotecaServiceImpl implements BibliotecaService{

    @Autowired
    @Lazy
    private BibliotecaRepository bibliotecaRepository;

    @Override
    public Biblioteca saveBiblioteca(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }

    @Override
    public void deleteBiblioteca(Long id) {
        bibliotecaRepository.deleteById(id);

    }

    @Override
    public Biblioteca updateBibioteca(Long id,Biblioteca biblioteca) {
        Biblioteca bibliotecaDB = bibliotecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con id: " + id));
        if (biblioteca.getNombre() != null && !biblioteca.getNombre().isBlank()) {
            bibliotecaDB.setNombre(biblioteca.getNombre());
        }

        if (biblioteca.getDescripcion() != null && !biblioteca.getDescripcion().isBlank()) {
            bibliotecaDB.setDescripcion(biblioteca.getDescripcion());
        }

        return bibliotecaRepository.save(bibliotecaDB);

    }
    // hacer luego la modificacion cuando tengamos la carpeta de error creada
    @Override
    public Biblioteca findBibiotecaById(Long id) {
            Optional<Biblioteca> biblioteca = bibliotecaRepository.findById(id);
            return biblioteca.orElse(null);

    }
}

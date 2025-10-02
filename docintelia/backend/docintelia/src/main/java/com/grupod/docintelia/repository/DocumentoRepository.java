package com.grupod.docintelia.repository;

import com.grupod.docintelia.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Long> {
    Optional<Documento> findByRutaArchivo(String rutaArchivo);
    Optional<Documento> findByTitulo(String titulo);


}

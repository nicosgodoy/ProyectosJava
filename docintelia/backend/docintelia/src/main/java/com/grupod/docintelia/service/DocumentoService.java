package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.DocumentoDTO;
import com.grupod.docintelia.dto.DocumentoResponseDTO;
import com.grupod.docintelia.model.Documento;
import com.grupod.docintelia.dto.ActualizarDocumentoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface DocumentoService {

    /**
    Documento saveDocument(Documento documento);
    Documento updateDocument(Long id_documento, Documento documento);
    void deleteDocument(Long id_documento);
    Documento findDocumentById(Long id_documento);
     */
    List<DocumentoResponseDTO>getAllDocuments();

    Path guardarArchivo(MultipartFile file, DocumentoDTO documentoDTO) throws IOException;

    Optional<Documento> findByRutaArchivo(String rutaArchivo);

    Optional<Documento> findByTitulo(String nombreArchivo);

    Optional<Documento> buscarPorId(Long id);

    boolean actualizarMetadatos(Long id, ActualizarDocumentoDTO dto);

    boolean eliminarDocumento(Long id);
}

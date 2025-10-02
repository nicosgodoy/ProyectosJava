    package com.grupod.docintelia.service;

    import com.grupod.docintelia.dto.DocumentoDTO;
    import com.grupod.docintelia.dto.DocumentoResponseDTO;
    import com.grupod.docintelia.dto.ResultadoBusquedaDTO;
    import com.grupod.docintelia.model.Biblioteca;
    import com.grupod.docintelia.model.Documento;
    import com.grupod.docintelia.repository.DocumentoRepository;
    import com.grupod.docintelia.dto.ActualizarDocumentoDTO;
    import com.grupod.docintelia.service.lucene.LuceneIndexService;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.time.LocalDate;
    import java.time.ZoneId;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Slf4j
    @Service
    public class DocumentoServiceImpl implements DocumentoService {

        @Autowired
        DocumentoRepository documentoRepository;

        @Autowired
        LuceneIndexService luceneIndexService;

        @Autowired
        private BibliotecaService bibliotecaService;

        @Value("${file.upload-dir}")
        private String uploadDir;

        @Override
        public List<DocumentoResponseDTO> getAllDocuments() {
            List<Documento> documentos = documentoRepository.findAll();
            List<DocumentoResponseDTO> documentoResponseDTOS = new ArrayList<>();
            for (Documento documento: documentos){
                DocumentoResponseDTO documentoResponseDTO = new DocumentoResponseDTO();

                documentoResponseDTO.setId_documento(documento.getId_documento());
                documentoResponseDTO.setIdioma(documento.getIdioma());
                documentoResponseDTO.setTitulo(documento.getTitulo());
                documentoResponseDTO.setFecha(documento.getFecha());
                documentoResponseDTO.setAutor(documento.getAutor());
                documentoResponseDTO.setEditorial(documento.getEditorial());
                documentoResponseDTO.setRutaArchivo(documento.getRutaArchivo());

                documentoResponseDTOS.add(documentoResponseDTO);
            }
            return documentoResponseDTOS;
        }

        @Override
        public Path guardarArchivo(MultipartFile file, DocumentoDTO documentoDTO) throws IOException {
            Path ruta = Paths.get(uploadDir).resolve(file.getOriginalFilename()).toAbsolutePath();
            Files.createDirectories(ruta.getParent());
            Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
            Biblioteca biblioteca = bibliotecaService.findBibiotecaById(1L);

            Documento doc = new Documento();
            doc.setTitulo(documentoDTO.getTitulo());
            doc.setAutor(documentoDTO.getAutor());
            doc.setEditorial(documentoDTO.getEditorial());
            doc.setIdioma(documentoDTO.getIdioma());
            doc.setRutaArchivo(ruta.toString());
            doc.setFecha(LocalDate.now());
            doc.setBiblioteca(biblioteca);

            documentoRepository.save(doc);

            return ruta;
        }

        @Override
        public Optional<Documento> findByRutaArchivo(String rutaArchivo) {
            return documentoRepository.findByRutaArchivo(rutaArchivo);
        }

        @Override
        public Optional<Documento> findByTitulo(String titulo) {
            return documentoRepository.findByTitulo(titulo);
        }

        @Override
        public Optional<Documento> buscarPorId(Long id) {
            return documentoRepository.findById(id);
        }

        public boolean actualizarMetadatos(Long id, ActualizarDocumentoDTO dto) {
            Optional<Documento> opt = documentoRepository.findById(id);
            if (opt.isEmpty()) return false;

            Documento doc = opt.get();

            if (dto.getTitulo() != null) {
                doc.setTitulo(dto.getTitulo());
                doc.setAutor(dto.getAutor());
                doc.setEditorial(dto.getEditorial());
                doc.setIdioma(dto.getIdioma());
            }

            if (dto.getFecha() != null) {
                doc.setFecha(dto.getFecha());
            }

            documentoRepository.save(doc);
            return true;
        }

        public boolean eliminarDocumento(Long id) {
            Optional<Documento> opt = documentoRepository.findById(id);
            if (opt.isEmpty()) return false;

            Documento doc = opt.get();

            try {
                // 1. Eliminar de Lucene
                luceneIndexService.eliminarDocumentoPorRuta(doc.getRutaArchivo());

                // 2. Eliminar archivo físico
                Files.deleteIfExists(Paths.get(doc.getRutaArchivo()));

                // 3. Eliminar de la base
                documentoRepository.deleteById(id);

                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }


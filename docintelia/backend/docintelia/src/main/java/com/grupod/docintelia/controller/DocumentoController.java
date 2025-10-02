package com.grupod.docintelia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupod.docintelia.dto.ActualizarDocumentoDTO;
import com.grupod.docintelia.dto.DocumentoDTO;
import com.grupod.docintelia.dto.DocumentoResponseDTO;
import com.grupod.docintelia.dto.ResultadoBusquedaDTO;
import com.grupod.docintelia.model.Documento;
import com.grupod.docintelia.service.DocumentoServiceImpl;
import com.grupod.docintelia.service.lucene.LuceneIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/documento")
public class DocumentoController {


    @Autowired
    private DocumentoServiceImpl documentoService;

    @Autowired
    private LuceneIndexService luceneIndexService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirPdf(@RequestParam("file") MultipartFile file,@RequestPart("documento") String documentoJson) {
        try {
            // Parsear el JSON manualmente
            ObjectMapper objectMapper = new ObjectMapper();
            DocumentoDTO documentoDTO = objectMapper.readValue(documentoJson, DocumentoDTO.class);

            // Calcular ruta donde se va a guardar el archivo (sin guardarlo aún)
            Path ruta = Paths.get("uploads").resolve(file.getOriginalFilename());
            String rutaNormalizada = ruta.toAbsolutePath().toString().replace("\\", "/");

            // Verificar si ya fue subido
            if (documentoService.findByRutaArchivo(rutaNormalizada).isPresent()) {
                return ResponseEntity.status(409).body("Este archivo ya fue subido y está indexado.");
            }

            //  Guardar el archivo en disco
            Path rutaFinal = documentoService.guardarArchivo(file, documentoDTO);

            // Indexar en Lucene
            luceneIndexService.indexarDocumento(rutaFinal);

            return ResponseEntity.ok("PDF subido correctamente: " + file.getOriginalFilename());

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir el archivo");
        }
    }



    //Indexar carpeta con muchos pdf
    @PostMapping("/indexar-carpeta")
    public ResponseEntity<String> indexarCarpeta(@RequestParam("ruta") String rutaAbsoluta) {
        try {
            Path carpeta = Paths.get(rutaAbsoluta);
            luceneIndexService.indexarCarpetaCompleta(carpeta);
            return ResponseEntity.ok("Indexación completa de carpeta: " + carpeta.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al indexar la carpeta: " + e.getMessage());
        }
    }


    //Buscar documentos
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam("q") String consulta) {
        try {
            List<ResultadoBusquedaDTO> resultados = luceneIndexService.buscar(consulta);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en la búsqueda: " + e.getMessage());
        }
    }

    @GetMapping("/gestion-documentos")
    public ResponseEntity<?> traerTodos() {
        try {
            List<DocumentoResponseDTO> resultados = documentoService.getAllDocuments();
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error en la búsqueda: " + e.getMessage());
        }
    }

    //Descargar documentos
    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarPorId(@PathVariable Long id) throws IOException {
        Optional<Documento> opt = documentoService.buscarPorId(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Documento doc = opt.get();
        Path filePath = Paths.get(doc.getRutaArchivo());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(404).body(null);
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getTitulo() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    //previsualizar documentos
    @GetMapping("/previsualizar/{id}")
    public ResponseEntity<Resource> previsualizarPorId(@PathVariable Long id) throws IOException {
        Optional<Documento> opt = documentoService.buscarPorId(id);

        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Documento doc = opt.get();
        Path filePath = Paths.get(doc.getRutaArchivo());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(404).body(null);
        }

        Resource resource = new UrlResource(filePath.toUri());

        // Se envía el encabezado Content-Disposition como 'inline' para que el navegador lo muestre.
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getTitulo() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarDocumento(@PathVariable Long id){
        boolean respuesta = documentoService.eliminarDocumento(id);

        if(respuesta){
            return ResponseEntity.ok("Documento eliminado");
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/actualizar-metadatos/{id}")
    public ResponseEntity<ActualizarDocumentoDTO> actualizarMetadatos(@PathVariable Long id,@RequestBody ActualizarDocumentoDTO documentoDTO){
        boolean respuesta = documentoService.actualizarMetadatos(id, documentoDTO);

        if(respuesta){
            return ResponseEntity.ok(documentoDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> buscarById(@PathVariable long id) {
        Optional<Documento> documentoOpt = documentoService.buscarPorId(id);

        if (documentoOpt.isPresent()) {
            Documento documento = documentoOpt.get();

            DocumentoDTO documentoDTO = new DocumentoDTO();
            documentoDTO.setTitulo(documento.getTitulo());
            documentoDTO.setAutor(documento.getAutor());
            documentoDTO.setEditorial(documento.getEditorial());
            documentoDTO.setIdioma(documento.getIdioma());

            return ResponseEntity.ok(documentoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



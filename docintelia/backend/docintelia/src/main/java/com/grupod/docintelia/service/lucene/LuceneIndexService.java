package com.grupod.docintelia.service.lucene;


import com.grupod.docintelia.model.Documento;
import com.grupod.docintelia.repository.DocumentoRepository;
import com.grupod.docintelia.dto.ResultadoBusquedaDTO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LuceneIndexService {

    @Autowired
    DocumentoRepository documentoRepository;

    private final String indexPath;

    public LuceneIndexService(@Value("${lucene.index-dir:lucene_index}") String indexPath) {
        this.indexPath = indexPath;
    }

    public void indexarDocumento(Path rutaArchivo) throws IOException {
        File archivo = rutaArchivo.toFile();

        if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("No es un archivo PDF");
        }

        // Leer texto del PDF
        PDDocument documento = PDDocument.load(archivo);
        PDFTextStripper stripper = new PDFTextStripper();
        String textoExtraido = stripper.getText(documento);
        documento.close();

        // Preparar índice
        FSDirectory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);

        // Crear y agregar documento al índice
        Document luceneDoc = new Document();
        luceneDoc.add(new StringField("ruta", archivo.getAbsolutePath(), Field.Store.YES));
        luceneDoc.add(new TextField("contenido", textoExtraido, Field.Store.YES));
        luceneDoc.add(new StringField("nombreArchivo", archivo.getName(), Field.Store.YES));

        writer.addDocument(luceneDoc);
        writer.close();

        System.out.println("Documento indexado: " + archivo.getName());
    }

    //Indexar carpeta con muchos pdf
    public void indexarCarpetaCompleta(Path carpeta) throws IOException {
        if (!Files.exists(carpeta) || !Files.isDirectory(carpeta)) {
            throw new IllegalArgumentException("Ruta inválida: " + carpeta);
        }

        Files.walk(carpeta)
                .filter(path -> path.toString().toLowerCase().endsWith(".pdf"))
                .forEach(path -> {
                    try {
                        // Si ya está en la base de datos, no lo indexamos de nuevo
                        if (documentoRepository.findByRutaArchivo(path.toString()).isPresent()) {
                            System.out.println("🔁 Ya indexado: " + path.getFileName());
                            return;
                        }

                        // Si no está, lo indexamos y lo guardamos
                        indexarDocumento(path);

                        // Guardamos el registro en PostgreSQL
                        Documento doc = new Documento();
                        doc.setTitulo(path.getFileName().toString());
                        doc.setRutaArchivo(path.toString());
                        doc.setFecha(LocalDate.now());

                        documentoRepository.save(doc);

                        System.out.println("Indexado: " + path.getFileName());

                    } catch (IOException e) {
                        System.err.println("Error al indexar: " + path + " - " + e.getMessage());
                    }
                });
    }


    private String obtenerExtracto(String contenido, String palabraClave) {
        if (contenido == null || palabraClave == null) return "";

        // Limpieza básica: eliminar saltos de línea y múltiples espacios
        contenido = contenido.replaceAll("[\\r\\n]+", " ").replaceAll(" +", " ").trim();

        // Eliminar posibles fragmentos de índice como líneas con muchos puntos o guiones
        contenido = contenido.replaceAll("(\\.{5,}|-{5,})", ""); // borra "....." o "-----"

        // Buscar palabra clave de forma insensible a mayúsculas
        int index = contenido.toLowerCase().indexOf(palabraClave.toLowerCase());

        // Si no se encuentra la palabra, devolver primer fragmento "limpio"
        if (index == -1) {
            return contenido.substring(0, Math.min(200, contenido.length())) + "...";
        }

        // Extraer una ventana de texto alrededor de la palabra clave
        int inicio = Math.max(0, index - 50);
        int fin = Math.min(contenido.length(), index + 100);

        String extracto = contenido.substring(inicio, fin).trim();

        // Reemplazar múltiples espacios nuevamente (por si quedaron)
        extracto = extracto.replaceAll(" +", " ");

        return "... " + extracto + " ...";
    }



    // Buscar documentos
    public List<ResultadoBusquedaDTO> buscar(String texto) throws Exception {
        List<ResultadoBusquedaDTO> resultados = new ArrayList<>();

        FSDirectory dir = FSDirectory.open(Paths.get(indexPath));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser("contenido", analyzer);
        Query query = parser.parse(texto);

        TopDocs topDocs = searcher.search(query, 20); // Máximo 5 resultados

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);

            String ruta = doc.get("ruta");
            Optional<Documento> opt = documentoRepository.findByRutaArchivo(ruta);
            if (opt.isEmpty()) continue;

            Documento documento = opt.get();

            String contenido = doc.get("contenido");
            String extracto = obtenerExtracto(contenido, texto);

            ResultadoBusquedaDTO resultado = new ResultadoBusquedaDTO();
            resultado.setId(documento.getId_documento());
            resultado.setNombreArchivo(doc.get("nombreArchivo"));
            resultado.setRutaArchivo(ruta);
            resultado.setScore(scoreDoc.score);
            resultado.setExtracto(extracto);
            resultado.setUrlDescarga("/api/documento/descargar/" + documento.getId_documento());

            resultados.add(resultado);
        }

        reader.close();
        return resultados;
    }

    public void eliminarDocumentoPorRuta(String rutaArchivo) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            writer.deleteDocuments(new Term("ruta", rutaArchivo));
            writer.commit();
        }
    }

}
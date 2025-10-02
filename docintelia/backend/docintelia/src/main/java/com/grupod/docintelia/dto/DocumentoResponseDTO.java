package com.grupod.docintelia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentoResponseDTO {
    private long id_documento;
    private String titulo;
    private String autor;
    private LocalDate fecha;
    private String editorial;
    private String idioma;
    private String rutaArchivo;
}





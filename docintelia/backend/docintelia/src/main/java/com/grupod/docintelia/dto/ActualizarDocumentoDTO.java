package com.grupod.docintelia.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarDocumentoDTO {
    private String titulo;
    private String autor;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private String editorial;
    private String idioma;
    private String rutaArchivo;
}

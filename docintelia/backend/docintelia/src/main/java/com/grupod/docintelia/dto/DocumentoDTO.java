package com.grupod.docintelia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoDTO {
    private String titulo;
    private String autor;
    private String editorial;
    private String idioma;
}

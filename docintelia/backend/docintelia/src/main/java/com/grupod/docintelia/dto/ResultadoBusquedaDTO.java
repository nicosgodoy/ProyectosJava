package com.grupod.docintelia.dto;

import lombok.Data;

@Data
public class ResultadoBusquedaDTO {
    private Long id;
    private String nombreArchivo;
    private String rutaArchivo;
    private float score;
    private String extracto;
    private String urlDescarga;
}

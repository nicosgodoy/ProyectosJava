package com.grupod.docintelia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id_documento;
    @NotBlank(message = "No puede estar vacio el titulo")
    private String titulo;
    private String autor;
    private LocalDate fecha;
    private String editorial;
    private String idioma;
    @Column(unique = true)
    private String rutaArchivo;
    @ManyToOne
    @JoinColumn(name = "id_biblioteca")
    private Biblioteca biblioteca;
}

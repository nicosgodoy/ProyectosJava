package com.grupod.docintelia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_biblioteca;
    private String nombre;
    private String descripcion;

    @OneToMany(
            mappedBy = "biblioteca",
            cascade = CascadeType.ALL
    )
    private List<Documento> listaDocumento;

    @OneToMany(
            mappedBy = "biblioteca",
            cascade = CascadeType.ALL
    )
    private List<Cuenta> listaCuenta;

}


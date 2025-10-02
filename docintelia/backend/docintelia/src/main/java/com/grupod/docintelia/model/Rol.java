
package com.grupod.docintelia.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Clase que representa los detalles y asociaciones de un Rol específico en el sistema Docintelia.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rol {

    // ID autogenerado para la entidad Rol.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    // Rol específico tomado del enum Roles.
    @Enumerated(EnumType.STRING)
    private Roles rol;

    // Relación Uno a Muchos con la entidad Cuenta.
    @OneToMany(mappedBy = "rol")
    private List<Cuenta> listaCuentas;
}

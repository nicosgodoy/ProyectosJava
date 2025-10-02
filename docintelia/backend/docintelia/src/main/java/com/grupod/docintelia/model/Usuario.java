package com.grupod.docintelia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "cuenta") // ⚠️ Evita recursión
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_usuario;
    @NotBlank(message = "No puede quedar en blanco el nombre")
    private String nombre;
    @NotBlank (message = "No puede quedar en blanco el apellido")
    private String apellido;
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Cuenta cuenta;

}
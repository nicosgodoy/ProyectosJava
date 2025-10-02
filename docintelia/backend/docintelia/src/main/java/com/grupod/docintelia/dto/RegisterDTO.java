package com.grupod.docintelia.dto;

import com.grupod.docintelia.model.Roles;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {

    @NotBlank (message = "No puede quedar en blanco el nombre")
    private String nombre;
    @NotBlank (message = "No puede quedar en blanco el apellido")
    private String apellido;
    @NotBlank (message = "No puede quedar en blanco el email")
    private String email;
    @NotBlank (message = "No puede quedar en blanco la contraseña")
    private String contrasenia;
    private Roles rol;
    private Long id_biblioteca;


}


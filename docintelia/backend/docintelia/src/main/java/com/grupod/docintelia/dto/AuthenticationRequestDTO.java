package com.grupod.docintelia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {

    @NotBlank (message = "no puede quedar en blanco el email")
    private String email;
    @NotBlank (message = "no puede quedar en blanco el email")
    private String password;
}

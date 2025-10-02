package com.grupod.docintelia.dto;

import com.grupod.docintelia.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCuentaDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;

    public  static UsuarioCuentaDTO convertirADTO(Usuario usuario) {
        return UsuarioCuentaDTO.builder()
                .id(usuario.getId_usuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getCuenta() != null ? usuario.getCuenta().getEmail() : null)
                .rol(usuario.getCuenta() != null && usuario.getCuenta().getRol() != null
                        ? usuario.getCuenta().getRol().getRol().name() : null)
                .build();
    }


}

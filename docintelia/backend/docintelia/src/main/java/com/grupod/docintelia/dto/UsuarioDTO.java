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
public class UsuarioDTO {

    private Long id;

    private String nombre;

    private String apellido;



    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId_usuario();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();

    }
}

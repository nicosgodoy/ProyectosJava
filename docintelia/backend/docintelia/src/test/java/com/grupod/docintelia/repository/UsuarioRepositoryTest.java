package com.grupod.docintelia.repository;

import com.grupod.docintelia.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setup(){

        usuario = Usuario.builder()
                .nombre("Nicolas")
                .apellido("Godoy")
                .build();
    }

    @Test
    void saveUsuario(){

        Usuario usuarioguardado = usuarioRepository.save(usuario);

        assertThat(usuarioguardado.getId_usuario()).isNotNull();
        assertThat(usuarioguardado.getNombre()).isEqualTo("Nicolas");
        assertThat(usuarioguardado.getApellido()).isEqualTo("Godoy");

    }

    @Test
    void updateUsuario(){

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        usuarioGuardado.setNombre("Emiliano");

        usuarioRepository.save(usuarioGuardado);

        assertThat(usuarioGuardado.getNombre()).isEqualTo("Emiliano");

    }

    @Test
    void buscarUsuarioPorId(){

        Usuario usuarioguardado = usuarioRepository.save(usuario);

        Usuario usuarioBuscado = usuarioRepository.findById(usuarioguardado.getId_usuario()).get();

        assertThat(usuarioBuscado.getId_usuario()).isNotNull();

    }

    @Test
    void eliminarUsuarioPorId(){

        Usuario usuarioGuardar = usuarioRepository.save(usuario);

        Optional<Usuario> usuarioBuscado = usuarioRepository.findById(usuarioGuardar.getId_usuario());

        usuarioRepository.deleteById(usuarioBuscado.get().getId_usuario());

        Optional<Usuario> usuarioEliminado = usuarioRepository.findById(usuarioGuardar.getId_usuario());

        assertThat(usuarioBuscado).isNotNull();
        assertThat(usuarioEliminado).isEmpty();
    }


}

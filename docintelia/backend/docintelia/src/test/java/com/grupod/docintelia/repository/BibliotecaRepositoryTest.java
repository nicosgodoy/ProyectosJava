package com.grupod.docintelia.repository;

import com.grupod.docintelia.model.Biblioteca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BibliotecaRepositoryTest {

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    private Biblioteca biblioteca;

    @BeforeEach
    void setup(){
        biblioteca = Biblioteca.builder()
                .nombre("Biblioteca Central")
                .descripcion("Biblioteca principal de la universidad")
                .build();
    }

    @Test
    void saveBiblioteca(){
        Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca);

        assertThat(bibliotecaGuardada.getId_biblioteca()).isNotNull();
        assertThat(bibliotecaGuardada.getNombre()).isEqualTo("Biblioteca Central");
        assertThat(bibliotecaGuardada.getDescripcion()).isEqualTo("Biblioteca principal de la universidad");
    }

    @Test
    void updateBiblioteca(){
        Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca);

        bibliotecaGuardada.setNombre("Biblioteca Actualizada");
        bibliotecaGuardada.setDescripcion("Descripción actualizada");

        bibliotecaRepository.save(bibliotecaGuardada);

        assertThat(bibliotecaGuardada.getNombre()).isEqualTo("Biblioteca Actualizada");
        assertThat(bibliotecaGuardada.getDescripcion()).isEqualTo("Descripción actualizada");
    }

    @Test
    void buscarBibliotecaPorId(){
        Biblioteca bibliotecaGuardada = bibliotecaRepository.save(biblioteca);

        Biblioteca bibliotecaBuscada = bibliotecaRepository.findById(bibliotecaGuardada.getId_biblioteca()).get();

        assertThat(bibliotecaBuscada.getId_biblioteca()).isNotNull();
        assertThat(bibliotecaBuscada.getNombre()).isEqualTo("Biblioteca Central");
        assertThat(bibliotecaBuscada.getDescripcion()).isEqualTo("Biblioteca principal de la universidad");
    }

    @Test
    void eliminarBibliotecaPorId(){
        Biblioteca bibliotecaGuardar = bibliotecaRepository.save(biblioteca);

        Optional<Biblioteca> bibliotecaBuscada = bibliotecaRepository.findById(bibliotecaGuardar.getId_biblioteca());

        bibliotecaRepository.deleteById(bibliotecaBuscada.get().getId_biblioteca());

        Optional<Biblioteca> bibliotecaEliminada = bibliotecaRepository.findById(bibliotecaGuardar.getId_biblioteca());

        assertThat(bibliotecaBuscada).isNotNull();
        assertThat(bibliotecaEliminada).isEmpty();
    }
}
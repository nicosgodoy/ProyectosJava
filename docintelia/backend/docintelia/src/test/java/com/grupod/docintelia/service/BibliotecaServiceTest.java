package com.grupod.docintelia.service;

import com.grupod.docintelia.model.Biblioteca;
import com.grupod.docintelia.repository.BibliotecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BibliotecaServiceTest {

    @Mock
    private BibliotecaRepository bibliotecaRepository;

    @InjectMocks
    private BibliotecaServiceImpl bibliotecaService;

    private Biblioteca biblioteca;

    @BeforeEach
    void setup(){
        biblioteca = Biblioteca.builder()
                .id_biblioteca(1L)
                .nombre("Biblioteca Central")
                .descripcion("Biblioteca principal de la universidad")
                .build();
    }

    @Test
    void saveBiblioteca(){
        when(bibliotecaRepository.save(any(Biblioteca.class))).thenReturn(biblioteca);

        Biblioteca bibliotecaGuardada = bibliotecaService.saveBiblioteca(biblioteca);

        assertThat(bibliotecaGuardada).isNotNull();
        assertThat(bibliotecaGuardada.getId_biblioteca()).isEqualTo(1L);
        assertThat(bibliotecaGuardada.getNombre()).isEqualTo("Biblioteca Central");
        assertThat(bibliotecaGuardada.getDescripcion()).isEqualTo("Biblioteca principal de la universidad");
        verify(bibliotecaRepository, times(1)).save(biblioteca);
    }

    @Test
    void deleteBiblioteca(){
        doNothing().when(bibliotecaRepository).deleteById(anyLong());

        bibliotecaService.deleteBiblioteca(1L);

        verify(bibliotecaRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateBiblioteca_Success(){
        Biblioteca bibliotecaExistente = Biblioteca.builder()
                .id_biblioteca(1L)
                .nombre("Biblioteca Antigua")
                .descripcion("Descripción antigua")
                .build();

        Biblioteca bibliotecaActualizada = Biblioteca.builder()
                .nombre("Biblioteca Actualizada")
                .descripcion("Descripción actualizada")
                .build();

        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(bibliotecaExistente));
        when(bibliotecaRepository.save(any(Biblioteca.class))).thenReturn(bibliotecaExistente);

        Biblioteca resultado = bibliotecaService.updateBibioteca(1L, bibliotecaActualizada);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Biblioteca Actualizada");
        assertThat(resultado.getDescripcion()).isEqualTo("Descripción actualizada");
        verify(bibliotecaRepository, times(1)).findById(1L);
        verify(bibliotecaRepository, times(1)).save(bibliotecaExistente);
    }

    @Test
    void updateBiblioteca_WithNullValues(){
        Biblioteca bibliotecaExistente = Biblioteca.builder()
                .id_biblioteca(1L)
                .nombre("Biblioteca Antigua")
                .descripcion("Descripción antigua")
                .build();

        Biblioteca bibliotecaActualizada = Biblioteca.builder()
                .nombre(null)
                .descripcion(null)
                .build();

        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(bibliotecaExistente));
        when(bibliotecaRepository.save(any(Biblioteca.class))).thenReturn(bibliotecaExistente);

        Biblioteca resultado = bibliotecaService.updateBibioteca(1L, bibliotecaActualizada);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Biblioteca Antigua");
        assertThat(resultado.getDescripcion()).isEqualTo("Descripción antigua");
        verify(bibliotecaRepository, times(1)).findById(1L);
        verify(bibliotecaRepository, times(1)).save(bibliotecaExistente);
    }

    @Test
    void updateBiblioteca_WithBlankValues(){
        Biblioteca bibliotecaExistente = Biblioteca.builder()
                .id_biblioteca(1L)
                .nombre("Biblioteca Antigua")
                .descripcion("Descripción antigua")
                .build();

        Biblioteca bibliotecaActualizada = Biblioteca.builder()
                .nombre("   ")
                .descripcion("   ")
                .build();

        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(bibliotecaExistente));
        when(bibliotecaRepository.save(any(Biblioteca.class))).thenReturn(bibliotecaExistente);

        Biblioteca resultado = bibliotecaService.updateBibioteca(1L, bibliotecaActualizada);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Biblioteca Antigua");
        assertThat(resultado.getDescripcion()).isEqualTo("Descripción antigua");
        verify(bibliotecaRepository, times(1)).findById(1L);
        verify(bibliotecaRepository, times(1)).save(bibliotecaExistente);
    }

    @Test
    void updateBiblioteca_BibliotecaNotFound(){
        Biblioteca bibliotecaActualizada = Biblioteca.builder()
                .nombre("Biblioteca Actualizada")
                .descripcion("Descripción actualizada")
                .build();

        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bibliotecaService.updateBibioteca(1L, bibliotecaActualizada))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Documento no encontrado con id: 1");

        verify(bibliotecaRepository, times(1)).findById(1L);
        verify(bibliotecaRepository, never()).save(any(Biblioteca.class));
    }

    @Test
    void findBibliotecaById_Success(){
        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(biblioteca));

        Biblioteca bibliotecaEncontrada = bibliotecaService.findBibiotecaById(1L);

        assertThat(bibliotecaEncontrada).isNotNull();
        assertThat(bibliotecaEncontrada.getId_biblioteca()).isEqualTo(1L);
        assertThat(bibliotecaEncontrada.getNombre()).isEqualTo("Biblioteca Central");
        assertThat(bibliotecaEncontrada.getDescripcion()).isEqualTo("Biblioteca principal de la universidad");
        verify(bibliotecaRepository, times(1)).findById(1L);
    }

    @Test
    void findBibliotecaById_NotFound(){
        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.empty());

        Biblioteca bibliotecaEncontrada = bibliotecaService.findBibiotecaById(1L);

        assertThat(bibliotecaEncontrada).isNull();
        verify(bibliotecaRepository, times(1)).findById(1L);
    }
}
package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.RolDTO;
import com.grupod.docintelia.model.Rol;
import com.grupod.docintelia.model.Roles;
import com.grupod.docintelia.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolAdmin;
    private Rol rolUser;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol();
        rolAdmin.setIdRol(1L);
        rolAdmin.setRol(Roles.ADMINISTRADOR);

        rolUser = new Rol();
        rolUser.setIdRol(2L);
        rolUser.setRol(Roles.USUARIO_REGISTRADO);
    }

    @Test
    void testGetAllRol() {
        // Preparar mock
        given(rolRepository.findAll()).willReturn(List.of(rolAdmin, rolUser));

        // Ejecutar método a testear
        List<RolDTO> result = rolService.getAllRol();

        // Validar resultados
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getRol()).isEqualTo("ADMIN");

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getRol()).isEqualTo("USER");

        // Verificar que se llamó al repo
        verify(rolRepository).findAll();
    }
}

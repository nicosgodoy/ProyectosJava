package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.RegisterDTO;
import com.grupod.docintelia.model.*;
import com.grupod.docintelia.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private RegisterDTO registerDTO;

    private Cuenta cuenta1;

    private Cuenta cuenta2;

    private Usuario usuario1;

    private Usuario usuario2;

    @BeforeEach
    void setup() {
        registerDTO = RegisterDTO.builder()
                .nombre("Nico")
                .apellido("Godoy")
                .email("nico@hotmail.com")
                .contrasenia("1234")
                .id_biblioteca(1L)
                .rol(Roles.ADMINISTRADOR)
                .build();

        usuario1 = Usuario.builder()
                .id_usuario(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .build();

        Rol rol = Rol.builder()
                .idRol(1L)
                .rol(Roles.ADMINISTRADOR) // o el enum que tengas
                .build();

        Biblioteca biblioteca = Biblioteca.builder()
                .id_biblioteca(1L)
                .nombre("Biblioteca Central")
                .build();

        cuenta1 = Cuenta.builder()
                .email("juan.perez@example.com")
                .contrasenia("secreta123")
                .usuario(usuario1)
                .rol(rol)
                .biblioteca(biblioteca)
                .build();

         usuario2 = Usuario.builder()
                .id_usuario(2L)
                .nombre("María")
                .apellido("Gómez")
                .build();

        Rol rol2 = Rol.builder()
                .idRol(2L)
                .rol(Roles.BIBLIOTECARIO) // Otro valor del enum, si lo tenés
                .build();

        Biblioteca biblioteca2 = Biblioteca.builder()
                .id_biblioteca(2L)
                .nombre("Biblioteca Sur")
                // otros campos...
                .build();

         cuenta2 = Cuenta.builder()
                .email("maria.gomez@example.com")
                .contrasenia("claveSegura456")
                .usuario(usuario2)
                .rol(rol2)
                .biblioteca(biblioteca2)
                .build();

    }

    @Test
    void  TestGetAllCuentas() {

        given(cuentaRepository.findAll()).willReturn(List.of(cuenta1, cuenta2));

        List<Cuenta> cuentaList = cuentaService.getAllCuentas();

        assertThat(cuentaList).isNotEmpty();
        assertThat(cuentaList.size()).isEqualTo(2);

    }



}


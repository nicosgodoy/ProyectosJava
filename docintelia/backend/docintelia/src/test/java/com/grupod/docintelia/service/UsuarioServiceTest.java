package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.UsuarioCuentaDTO;
import com.grupod.docintelia.dto.UsuarioDTO;
import com.grupod.docintelia.model.*;
import com.grupod.docintelia.repository.BibliotecaRepository;
import com.grupod.docintelia.repository.CuentaRepository;
import com.grupod.docintelia.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private BibliotecaRepository bibliotecaRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {

        Rol rolAdmin = new Rol();
        rolAdmin.setRol(Roles.ADMINISTRADOR);

        Rol rolUsu = new Rol();
        rolUsu.setRol(Roles.USUARIO_REGISTRADO);

        Biblioteca biblioteca = bibliotecaRepository.save(Biblioteca.builder()
                .nombre("Biblioteca Central")
                .build());


        // Crear cuentas
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setEmail("usuario1@email.com");
        cuenta1.setContrasenia("123456");
        cuenta1.setRol(rolAdmin);
        cuenta1.setBiblioteca(biblioteca);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setEmail("usuario2@email.com");
        cuenta2.setContrasenia("654321");
        cuenta2.setRol(rolUsu);
        cuenta2.setBiblioteca(biblioteca);

        // Crear usuarios con cuentas
        usuario1 = new Usuario();
        usuario1.setId_usuario(1L);
        usuario1.setNombre("Juan");
        usuario1.setApellido("Perez");
        usuario1.setCuenta(cuenta1);

        usuario2 = new Usuario();
        usuario2.setId_usuario(2L);
        usuario2.setNombre("Ana");
        usuario2.setApellido("Lopez");
        usuario2.setCuenta(cuenta2);
    }

    @Test
    void TestGetAllUsers() {
        // Preparar mock
        given(usuarioRepository.findAll()).willReturn(List.of(usuario1, usuario2));

        // Ejecutar método a testear
        List<UsuarioCuentaDTO> result = usuarioService.getAllUsers();

        // Validar resultados
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getNombre()).isEqualTo("Juan");
        assertThat(result.get(0).getEmail()).isEqualTo("usuario1@email.com");
        assertThat(result.get(0).getRol()).isEqualTo("ADMINISTRADOR");

        assertThat(result.get(1).getNombre()).isEqualTo("Ana");
        assertThat(result.get(1).getEmail()).isEqualTo("usuario2@email.com");
        assertThat(result.get(1).getRol()).isEqualTo("USUARIO_REGISTRADO");
    }

    @Test
    void TestCreateUser(){
        Biblioteca biblioteca = Biblioteca.builder()
                .id_biblioteca(3L)
                .nombre("Biblioteca Central")
                .build();

        Rol rol = Rol.builder().rol(Roles.USUARIO_REGISTRADO).build();

        Usuario usuario = Usuario.builder()
                .id_usuario(3L)
                .nombre("Nicolas")
                .apellido("Godoy")
                .build();

        Cuenta cuenta = Cuenta.builder()
                .email("nicolas@email.com")
                .contrasenia("123456")
                .usuario(usuario)
                .rol(rol)
                .biblioteca(biblioteca)
                .build();

        usuario.setCuenta(cuenta);

        given(usuarioRepository.save(usuario)).willReturn(usuario);

        UsuarioDTO dto = usuarioService.createUser(usuario);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getNombre()).isEqualTo("Nicolas");
        assertThat(dto.getApellido()).isEqualTo("Godoy");
    }

    @Test
    void TestUpdateUser(){

        Usuario usumodificado = new Usuario();
        usumodificado.setNombre("Federico");
        usumodificado.setApellido("Delgado");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario1));
        given(usuarioRepository.save(usuario1)).willReturn(usuario1);

        UsuarioDTO dto = usuarioService.updateUser(usumodificado,1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Federico");
        assertThat(dto.getApellido()).isEqualTo("Delgado");

        verify(usuarioRepository).save(usuario1);
    }

    @Test
    void TestFindByIdUser(){

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario1));

        UsuarioDTO dto = usuarioService.findByIdUser(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Juan");
        assertThat(dto.getApellido()).isEqualTo("Perez");

    }

    @Test
    void TestDeleteUser() {
        Usuario usuario = Usuario.builder()
                .id_usuario(3L)
                .nombre("Nicolas")
                .apellido("Godoy")
                .build();

        Rol rolBiblio = new Rol();
        rolBiblio.setRol(Roles.BIBLIOTECARIO);

        Biblioteca biblioteca = Biblioteca.builder()
                .nombre("Biblioteca Central")
                .build();

        Cuenta cuenta = Cuenta.builder()
                .email("nicolas@test.com")
                .contrasenia("123456")
                .usuario(usuario)
                .rol(rolBiblio)
                .biblioteca(biblioteca)
                .build();

        usuario.setCuenta(cuenta);

        given(usuarioRepository.findById(3L)).willReturn(Optional.of(usuario));
        willDoNothing().given(usuarioRepository).delete(usuario);

        usuarioService.deleteUser(3L);

        verify(usuarioRepository).delete(usuario);
    }

}

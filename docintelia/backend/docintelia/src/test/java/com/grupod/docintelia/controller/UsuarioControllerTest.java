package com.grupod.docintelia.controller;

import com.grupod.docintelia.dto.AuthResponseDTO;
import com.grupod.docintelia.dto.AuthenticationRequestDTO;
import com.grupod.docintelia.dto.UsuarioCuentaDTO;
import com.grupod.docintelia.dto.UsuarioDTO;
import com.grupod.docintelia.model.*;
import com.grupod.docintelia.repository.BibliotecaRepository;
import com.grupod.docintelia.repository.RolRepository;
import com.grupod.docintelia.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    private String token;

    @BeforeEach
    void authenticate() {
        // DTO con email y password
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO();
        authRequest.setEmail("admin@admin.com");
        authRequest.setPassword("admin123");

        // Hacemos POST a /api/auth/authenticate
        ResponseEntity<AuthResponseDTO> response = testRestTemplate.postForEntity(
                "/api/auth/authenticate",
                authRequest,
                AuthResponseDTO.class
        );

        // Obtenemos token
        if (response.getBody() != null && response.getBody().getToken() != null) {
            this.token = response.getBody().getToken();
        } else {
            fail("No se pudo obtener el token. Verifica que el usuario admin@admin.com exista.");
        }
    }

    @BeforeEach
    void cargarUsuarioEnDB() {

        if (usuarioRepository.findAll().size()==1) {

            Biblioteca biblioteca = new Biblioteca();
            biblioteca.setNombre("Biblioteca Central");
            biblioteca = bibliotecaRepository.save(biblioteca);

            // Crear rol
            Rol rol = new Rol();
            rol.setRol(Roles.ADMINISTRADOR); // Asumiendo que tenés un Enum llamado Roles
            rol = rolRepository.save(rol);

            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setNombre("Nicolas");
            usuario.setApellido("Perez");


            // Crear cuenta
            Cuenta cuenta = new Cuenta();
            cuenta.setEmail("nico@correo.com");
            cuenta.setContrasenia("123456");
            cuenta.setRol(rol);
            cuenta.setBiblioteca(biblioteca);
            cuenta.setUsuario(usuario);

            usuario.setCuenta(cuenta);

            // Guardar en cascada (o por separado si no tenés cascada)
            usuarioRepository.save(usuario);

            Rol rol2 = new Rol();
            rol2.setRol(Roles.USUARIO_REGISTRADO); // Asumiendo que tenés un Enum llamado Roles
            rol2 = rolRepository.save(rol2);

            // Crear usuario
            Usuario usuario2 = new Usuario();
            usuario2.setNombre("Emiliano");
            usuario2.setApellido("Godoy");


            // Crear cuenta
            Cuenta cuenta2 = new Cuenta();
            cuenta2.setEmail("emi@correo.com");
            cuenta2.setContrasenia("654321");
            cuenta2.setRol(rol2);
            cuenta2.setBiblioteca(biblioteca);
            cuenta2.setUsuario(usuario2);

            usuario2.setCuenta(cuenta2);

            // Guardar en cascada (o por separado si no tenés cascada)
            usuarioRepository.save(usuario2);
        }

    }

    @Test
    @Order(1)
    void testGetAllUsersProtectedEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Hacemos GET a endpoint protegido
        ResponseEntity<UsuarioCuentaDTO[]> response = testRestTemplate.exchange(
                "/api/usuario/lista",  // ruta protegida
                HttpMethod.GET,
                entity,
                UsuarioCuentaDTO[].class
        );

        // Verificamos respuesta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<UsuarioCuentaDTO> usuarios = Arrays.asList(response.getBody());


        assertEquals(3, usuarios.size());
        assertEquals("Admin",usuarioRepository.findById(1L).get().getNombre());
        assertEquals("Nicolas",usuarioRepository.findById(2L).get().getNombre());



    }

    @Test
    @Order(2)
    void findByIdUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Hacemos GET a endpoint protegido
        ResponseEntity<UsuarioDTO> response = testRestTemplate.exchange(
                "/api/usuario/find-by-id/{id}",
                HttpMethod.GET,
                entity,
                UsuarioDTO.class,
                3L

        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UsuarioDTO usuario = response.getBody();
        assertNotNull(usuario);
        assertEquals("Emiliano", usuario.getNombre());
        assertEquals("Godoy", usuario.getApellido());

    }

    @Test
    @Order(3)
    void updateUser(){

        UsuarioDTO usuarioDTOActualizado = UsuarioDTO.builder()
                .nombre("Esteban")
                .apellido("Lopez")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<UsuarioDTO> entity = new HttpEntity<>(usuarioDTOActualizado,headers);

        // Hacemos PUT a endpoint protegido
        ResponseEntity<UsuarioDTO> response = testRestTemplate.exchange(
                "/api/usuario/{id}",
                HttpMethod.PUT,
                entity,
                UsuarioDTO.class,
                2L
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        UsuarioDTO usuarioDTOactualizado = response.getBody();

        assertEquals("Esteban",usuarioDTOactualizado.getNombre());
        assertEquals("Lopez",usuarioDTOactualizado.getApellido());

    }

    @Test
    @Order(4)
    void deleteUser(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> string = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/usuario/{id}",
                HttpMethod.DELETE,
                string,
                String.class,
                2L
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Usuario eliminado exitosamente", response.getBody());




    }

}


package com.grupod.docintelia.service;

import com.grupod.docintelia.config.JwtService;
import com.grupod.docintelia.dto.AuthResponseDTO;
import com.grupod.docintelia.dto.AuthenticationRequestDTO;
import com.grupod.docintelia.dto.RegisterDTO;
import com.grupod.docintelia.model.Biblioteca;
import com.grupod.docintelia.model.Cuenta;
import com.grupod.docintelia.model.Rol;
import com.grupod.docintelia.model.Usuario;
import com.grupod.docintelia.repository.BibliotecaRepository;
import com.grupod.docintelia.repository.CuentaRepository;
import com.grupod.docintelia.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UsuarioService usuarioService;
    private final CuentaRepository cuentaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RolRepository rolRepository;
    private final BibliotecaRepository bibliotecaRepository;


    @Override
    public AuthResponseDTO register(RegisterDTO request) {

        Biblioteca biblioteca = bibliotecaRepository.findById(request.getId_biblioteca())
                .orElseThrow(() -> new RuntimeException("Biblioteca no encontrada"));

        // Obtener Rol desde la base de datos usando enum
        Rol rolEntidad = rolRepository.findByRol(request.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (cuentaRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con ese email");
        }

        // Crear Usuario
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .build();

        // Crear Cuenta
        Cuenta cuenta = Cuenta.builder()
                .email(request.getEmail())
                .contrasenia(passwordEncoder.encode(request.getContrasenia()))
                .usuario(usuario)
                .rol(rolEntidad)
                .biblioteca(biblioteca)
                .build();

        // Asociar la cuenta al usuario (lado inverso)
        usuario.setCuenta(cuenta);
        ;

        // Guardar el usuario → por cascada se guarda también la cuenta
        usuarioService.createUser(usuario);

        // Generar token JWT
        var jwtToken = jwtService.generateToken(cuenta);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();
    }


    @Override
    public AuthResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var cuenta = cuentaRepository.findUserByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(cuenta);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .rol(cuenta.getRol().getRol().name()) // <- enviamos el rol del usuario
                .build();

    }

}
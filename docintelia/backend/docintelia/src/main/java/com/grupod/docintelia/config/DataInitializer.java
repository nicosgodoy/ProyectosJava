package com.grupod.docintelia.config;


import com.grupod.docintelia.model.*;
import com.grupod.docintelia.repository.BibliotecaRepository;
import com.grupod.docintelia.repository.CuentaRepository;
import com.grupod.docintelia.repository.RolRepository;
import com.grupod.docintelia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final RolRepository rolRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public void run(String... args) throws Exception {
        //  Crear todos los roles del enum si no existen
        for (Roles rolEnum : Roles.values()) {
            rolRepository.findByRol(rolEnum)
                    .orElseGet(() -> rolRepository.save(Rol.builder().rol(rolEnum).build()));
        }

        //  Crear la biblioteca si no existe
        Biblioteca biblioteca = bibliotecaRepository.findById(1L).orElseGet(() -> {
            Biblioteca nueva = new Biblioteca();
            nueva.setNombre("Biblioteca Central");
            return bibliotecaRepository.save(nueva);
        });

        //  Crear cuenta admin si no existe
        cuentaRepository.findUserByEmail("admin@admin.com").ifPresentOrElse(cuentaExistente -> {
            var token = jwtService.generateToken(cuentaExistente);
            System.out.println("⚠️ Administrador ya existe");
            System.out.println("🔑 Token existente: " + token);
        }, () -> {
            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Principal")
                    .build();
            admin = usuarioRepository.save(admin);

            //  buscar el rol ADMINISTRADOR
            Rol rolAdmin = rolRepository.findByRol(Roles.ADMINISTRADOR)
                    .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

            Cuenta cuenta = Cuenta.builder()
                    .email("admin@admin.com")
                    .contrasenia(passwordEncoder.encode("admin123"))
                    .usuario(admin)
                    .rol(rolAdmin)
                    .biblioteca(biblioteca)
                    .build();
            cuentaRepository.save(cuenta);

            var token = jwtService.generateToken(cuenta);
            System.out.println("✅ Administrador creado");
            System.out.println("🧑 Usuario: admin@admin.com");
            System.out.println("🔐 Contraseña: admin123");
            System.out.println("🔑 Token nuevo: " + token);
        });
    }
}

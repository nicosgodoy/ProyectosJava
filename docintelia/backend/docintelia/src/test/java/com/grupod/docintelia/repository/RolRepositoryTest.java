package com.grupod.docintelia.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.grupod.docintelia.model.Rol;
import com.grupod.docintelia.model.Roles;
import com.grupod.docintelia.repository.RolRepository;

@DataJpaTest
public class RolRepositoryTest {

    @Autowired
    private RolRepository rolRepository;

    @Test
    public void testGuardarYBuscarRol() {
        Rol rol = new Rol();
        rol.setRol(Roles.ADMINISTRADOR);

        rol = rolRepository.save(rol);
        Optional<Rol> encontrado = rolRepository.findById(rol.getIdRol());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getRol()).isEqualTo(Roles.ADMINISTRADOR);
    }
}

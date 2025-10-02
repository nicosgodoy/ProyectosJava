package com.grupod.docintelia.repository;

import com.grupod.docintelia.model.Rol;
import com.grupod.docintelia.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByRol(Roles rol);
}

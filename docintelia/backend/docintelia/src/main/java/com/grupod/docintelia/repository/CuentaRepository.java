package com.grupod.docintelia.repository;

import com.grupod.docintelia.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findUserByEmail(String email);
    boolean existsByEmail(String email);
}

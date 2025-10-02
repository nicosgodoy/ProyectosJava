package com.grupod.docintelia.service;

import com.grupod.docintelia.model.Cuenta;

import java.util.List;

public interface CuentaService {
    List<Cuenta> getAllCuentas();

    Cuenta findByIdCuenta(Long id);

    Cuenta updateCuenta(Cuenta cuenta, Long id);

    Cuenta createCuenta(Cuenta cuenta);
}

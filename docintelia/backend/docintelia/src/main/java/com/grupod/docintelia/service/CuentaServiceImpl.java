package com.grupod.docintelia.service;

import com.grupod.docintelia.model.Cuenta;
import com.grupod.docintelia.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService{

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public List<Cuenta> getAllCuentas(){
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta findByIdCuenta(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        return cuenta.orElse(null);
    }

    @Override
    public Cuenta updateCuenta(Cuenta cuenta, Long id) {
        Cuenta cuentaFound = cuentaRepository.findById(id).get();
        cuentaFound.setEmail(cuenta.getEmail());
        cuentaFound.setContrasenia(cuenta.getContrasenia());
        cuentaFound.setUsuario(cuenta.getUsuario());
        cuentaFound.setBiblioteca(cuenta.getBiblioteca());
        cuentaFound.setRol(cuenta.getRol());

        return cuentaRepository.save(cuentaFound);
    }

    @Override
    public Cuenta createCuenta(Cuenta cuenta) {
        if (cuentaRepository.existsByEmail(cuenta.getEmail())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email.");
        }
        return cuentaRepository.save(cuenta);
    }
}

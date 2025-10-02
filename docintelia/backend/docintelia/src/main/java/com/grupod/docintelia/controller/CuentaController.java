package com.grupod.docintelia.controller;

import com.grupod.docintelia.model.Cuenta;
import com.grupod.docintelia.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;

    @GetMapping("/get-all")
    public List<Cuenta> getAllCuenta(){
        return cuentaService.getAllCuentas();
    }

    @GetMapping("/{id}")
    public Cuenta findByIdCuenta(@PathVariable Long id){
        return cuentaService.findByIdCuenta(id);
    }

    @PutMapping("/update/{id}")
    public Cuenta updateCuenta(@RequestBody  @Valid Cuenta cuenta, @PathVariable Long id){
        return cuentaService.updateCuenta(cuenta, id);
    }

}

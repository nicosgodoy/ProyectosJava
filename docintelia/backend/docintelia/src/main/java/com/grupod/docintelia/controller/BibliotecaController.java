package com.grupod.docintelia.controller;

import com.grupod.docintelia.model.Biblioteca;
import com.grupod.docintelia.service.BibliotecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    BibliotecaService bibliotecaService;

    @PostMapping("/save")
    public Biblioteca saveBiblioteca(@RequestBody Biblioteca biblioteca) {
        return bibliotecaService.saveBiblioteca(biblioteca);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBiblioteca(@PathVariable("id") Long id){
        bibliotecaService.deleteBiblioteca(id);
        return "Biblioteca borrada Exitosamente ";
    }

    @PutMapping("/update/{id}")
    public Biblioteca updateBibioteca(@PathVariable Long id,@RequestBody Biblioteca biblioteca){
        return bibliotecaService.updateBibioteca(id,biblioteca);
    }

    @GetMapping("/find-by-id/{id}")
    public Biblioteca findBibliotecaById(@PathVariable Long id){
        return bibliotecaService.findBibiotecaById(id);
    }

}

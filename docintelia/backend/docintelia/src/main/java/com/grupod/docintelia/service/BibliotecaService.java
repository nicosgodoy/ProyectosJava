package com.grupod.docintelia.service;

import com.grupod.docintelia.model.Biblioteca;

public interface BibliotecaService {

    Biblioteca saveBiblioteca(Biblioteca biblioteca);
    void deleteBiblioteca(Long id);
    Biblioteca updateBibioteca(Long id,Biblioteca biblioteca);
    Biblioteca findBibiotecaById(Long id);


}

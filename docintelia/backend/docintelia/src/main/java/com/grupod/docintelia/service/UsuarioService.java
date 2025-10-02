package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.UsuarioCuentaDTO;
import com.grupod.docintelia.dto.UsuarioDTO;
import com.grupod.docintelia.model.Usuario;

import java.util.List;

public interface UsuarioService {

        List<UsuarioCuentaDTO> getAllUsers(); // OK: salida con DTO

        UsuarioDTO findByIdUser(Long id); // OK: salida con DTO

        UsuarioDTO updateUser(Usuario user, Long id); // Cambiar entrada y salida a DTO

        String deleteUser(Long id);

        UsuarioDTO createUser(Usuario user) ;






}

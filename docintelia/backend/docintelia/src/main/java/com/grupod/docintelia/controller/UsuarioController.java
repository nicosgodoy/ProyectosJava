package com.grupod.docintelia.controller;

import com.grupod.docintelia.dto.UsuarioCuentaDTO;
import com.grupod.docintelia.dto.UsuarioDTO;
import com.grupod.docintelia.model.Usuario;
import com.grupod.docintelia.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioCuentaDTO>> getAllUsers() {
         List<UsuarioCuentaDTO> usuarioCuentaDTOList = usuarioService.getAllUsers();
         return ResponseEntity.ok(usuarioCuentaDTOList);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<UsuarioDTO>  findByIdUser(@PathVariable Long id){
        UsuarioDTO usuarioDTO =  usuarioService.findByIdUser(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@RequestBody @Valid Usuario user, @PathVariable Long id){
        UsuarioDTO usuarioDTO = usuarioService.updateUser(user, id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
       String mensaje = usuarioService.deleteUser(id);
        return ResponseEntity.ok(mensaje);
    }
}

package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.UsuarioCuentaDTO;
import com.grupod.docintelia.dto.UsuarioDTO;
import com.grupod.docintelia.error.ResourceNotFoundException;
import com.grupod.docintelia.model.Cuenta;
import com.grupod.docintelia.model.Usuario;
import com.grupod.docintelia.repository.CuentaRepository;
import com.grupod.docintelia.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Override
    public UsuarioDTO createUser(Usuario user) {
        Usuario usuario = usuarioRepository.save(user);
        return new UsuarioDTO(usuario);
    }


    @Override
    public List<UsuarioCuentaDTO> getAllUsers() {
        List<UsuarioCuentaDTO> usuarioCuentaDTOList = new ArrayList<>();
        List<Usuario> usuarioList = usuarioRepository.findAll();

        for (Usuario item : usuarioList) {
            try {
                Cuenta cuenta = item.getCuenta();
                //esto hace que se si no tiene cuenta se guarde en el log y la ejecucion continue
                if (cuenta == null) {
                    log.warn("Usuario con ID {} no tiene cuenta asociada", item.getId_usuario());
                    continue;
                }
                //esto hace que si no tiene un rol asignado se guarde en el log y que la ejecucion continue
                if (cuenta.getRol() == null || cuenta.getRol().getRol() == null) {
                    log.warn("La cuenta del usuario con ID {} no tiene un rol válido", item.getId_usuario());
                    continue;
                }

                UsuarioCuentaDTO dto = new UsuarioCuentaDTO();
                dto.setId(item.getId_usuario());
                dto.setNombre(item.getNombre());
                dto.setApellido(item.getApellido());
                dto.setEmail(cuenta.getEmail() != null ? cuenta.getEmail() : "Sin email");
                dto.setRol(cuenta.getRol().getRol().name());

                usuarioCuentaDTOList.add(dto);

            } catch (Exception ex) {
                log.error("Error al procesar usuario con ID {}", item.getId_usuario(), ex);
            }
        }

        return usuarioCuentaDTOList;
    }

    public UsuarioDTO findByIdUser(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario con ID {} no fue encontrado", id);
                    return new ResourceNotFoundException("Usuario solicitado no fue encontrado");
                });
        return new UsuarioDTO(usuario);
    }

    @Override
    public UsuarioDTO updateUser(Usuario user, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));


        usuario.setNombre(user.getNombre());
        usuario.setApellido(user.getApellido());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new UsuarioDTO(usuarioGuardado);
    }

    @Override
    public String deleteUser(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
        usuarioRepository.delete(usuario);
        return "Usuario eliminado exitosamente";
    }




}

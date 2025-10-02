package com.grupod.docintelia.service;

import com.grupod.docintelia.dto.RolDTO;
import com.grupod.docintelia.model.Rol;
import com.grupod.docintelia.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

   /* @Override
    public Rol createRole(Rol rol) {
        return rolRepository.save(rol);
    }*/

    @Override
    public List<RolDTO> getAllRol() {
        List <RolDTO> listaRolDTO = new ArrayList<>();
        List <Rol> listaRol = rolRepository.findAll();
        for (Rol item : listaRol){
            RolDTO rolDTO = new RolDTO();
            rolDTO.setId(item.getIdRol());
            rolDTO.setRol(item.getRol().name());

            listaRolDTO.add(rolDTO);
        }
        return listaRolDTO;
    }


   /* @Override
    public Rol updateRol(Rol rol, Long id) {
        Rol rolBuscar = rolRepository.findById(rol.getIdRol()).get();
        rolBuscar.setRol(rol.getRol());
        return rolRepository.save(rolBuscar);
    }*/

   /* @Override
    public void deleteRolById(Long id) {
        rolRepository.deleteById(id);
    }*/
}

package com.peluqueriacanina.Springpeluqueriacanina.service;

import com.peluqueriacanina.Springpeluqueriacanina.dto.MascotaDTO;
import com.peluqueriacanina.Springpeluqueriacanina.model.Mascota;


import java.util.List;


public interface MascotaService {


     MascotaDTO guardarMascota(MascotaDTO dto);
     MascotaDTO obtenerMascotaPorId(int id);
     List<MascotaDTO> obtenerTodas();
     MascotaDTO modificarMascota(int id, MascotaDTO dto);
     void eliminarMascota(int id);
}

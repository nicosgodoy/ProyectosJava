package com.peluqueriacanina.Springpeluqueriacanina.service;

import com.peluqueriacanina.Springpeluqueriacanina.dto.MascotaDTO;
import com.peluqueriacanina.Springpeluqueriacanina.model.Duenio;
import com.peluqueriacanina.Springpeluqueriacanina.model.Mascota;
import com.peluqueriacanina.Springpeluqueriacanina.repository.DuenioRepository;
import com.peluqueriacanina.Springpeluqueriacanina.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaServiceImpl implements MascotaService{

    @Autowired
    MascotaRepository mascotaRepo;

    @Autowired
    DuenioRepository duenioRepo;

        @Override
        public MascotaDTO guardarMascota(MascotaDTO dto) {
            Duenio duenio = new Duenio();
            duenio.setNombre(dto.getNombreDuenio());
            duenio.setCel_Duenio(dto.getCelDuenio());
            duenioRepo.save(duenio);

            Mascota mascota = new Mascota();
            mascota.setNombre(dto.getNombreMascota());
            mascota.setRaza(dto.getRaza());
            mascota.setColor(dto.getColor());
            mascota.setAlergico(dto.getAlergico());
            mascota.setAtencion_especial(dto.getAtencionEspecial());
            mascota.setObservaciones(dto.getObservaciones());
            mascota.setUnDuenio(duenio);

            mascotaRepo.save(mascota);
            dto.setId(mascota.getNum_cliente());
            return dto;
        }

        @Override
        public MascotaDTO obtenerMascotaPorId(int id) {
            Mascota mascota = mascotaRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("No encontrada"));

            MascotaDTO dto = new MascotaDTO();
            dto.setId(mascota.getNum_cliente());
            dto.setNombreMascota(mascota.getNombre());
            dto.setRaza(mascota.getRaza());
            dto.setColor(mascota.getColor());
            dto.setAlergico(mascota.getAlergico());
            dto.setAtencionEspecial(mascota.getAtencion_especial());
            dto.setObservaciones(mascota.getObservaciones());
            dto.setNombreDuenio(mascota.getNombre());
            dto.setCelDuenio(mascota.getUnDuenio().getCel_Duenio());

            return dto;
        }

        @Override
        public List<MascotaDTO> obtenerTodas() {
            List<Mascota> mascotas = mascotaRepo.findAll();
            return mascotas.stream().map(m -> {
                MascotaDTO dto = new MascotaDTO();
                dto.setId(m.getNum_cliente());
                dto.setNombreMascota(m.getNombre());
                dto.setRaza(m.getRaza());
                dto.setColor(m.getColor());
                dto.setAlergico(m.getAlergico());
                dto.setAtencionEspecial(m.getAtencion_especial());
                dto.setObservaciones(m.getObservaciones());
                dto.setNombreDuenio(m.getUnDuenio().getNombre());
                dto.setCelDuenio(m.getUnDuenio().getCel_Duenio());
                return dto;
            }).toList();
        }

        @Override
        public MascotaDTO modificarMascota(int id, MascotaDTO dto) {
            Mascota mascota = mascotaRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("No encontrada"));

            mascota.setNombre(dto.getNombreMascota());
            mascota.setRaza(dto.getRaza());
            mascota.setColor(dto.getColor());
            mascota.setAlergico(dto.getAlergico());
            mascota.setAtencion_especial(dto.getAtencionEspecial());
            mascota.setObservaciones(dto.getObservaciones());

            Duenio duenio = mascota.getUnDuenio();
            duenio.setNombre(dto.getNombreDuenio());
            duenio.setCel_Duenio(dto.getCelDuenio());
            duenioRepo.save(duenio);

            mascotaRepo.save(mascota);

            return dto;
        }

        @Override
        public void eliminarMascota(int id) {
            mascotaRepo.deleteById(id);

            duenioRepo.deleteById(id);
        }
    }






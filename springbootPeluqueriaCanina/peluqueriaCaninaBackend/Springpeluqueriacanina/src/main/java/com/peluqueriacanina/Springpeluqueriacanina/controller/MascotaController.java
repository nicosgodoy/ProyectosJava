package com.peluqueriacanina.Springpeluqueriacanina.controller;

import com.peluqueriacanina.Springpeluqueriacanina.dto.MascotaDTO;
import com.peluqueriacanina.Springpeluqueriacanina.model.Duenio;
import com.peluqueriacanina.Springpeluqueriacanina.model.Mascota;
import com.peluqueriacanina.Springpeluqueriacanina.repository.MascotaRepository;
import com.peluqueriacanina.Springpeluqueriacanina.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/peluqueriacanina")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MascotaRepository mascotaRepository;


    @PostMapping ("/mascotas")
    public ResponseEntity<String> guardarMascota(@RequestBody MascotaDTO dto) {
        mascotaService.guardarMascota(dto);
        return ResponseEntity.ok("Mascota  guardado correctamente.");
    }

    @GetMapping ("/mascotas")
    public List<MascotaDTO> obtenerTodas() {
    List<Mascota> mascotas = mascotaRepository.findAll();

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
    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> obtenerPorId(@PathVariable int id) {
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(id);

        if (mascotaOpt.isPresent()) {
            Mascota m = mascotaOpt.get();
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
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> modificarMascota(@PathVariable int id, @RequestBody MascotaDTO dto) {
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(id);

        if (mascotaOpt.isPresent()) {
            Mascota mascota = mascotaOpt.get();
            mascota.setNombre(dto.getNombreMascota());
            mascota.setRaza(dto.getRaza());
            mascota.setColor(dto.getColor());
            mascota.setAlergico(dto.getAlergico());
            mascota.setAtencion_especial(dto.getAtencionEspecial());
            mascota.setObservaciones(dto.getObservaciones());

            Duenio duenio = mascota.getUnDuenio();
            duenio.setNombre(dto.getNombreDuenio());
            duenio.setCel_Duenio(dto.getCelDuenio());

            mascota.setUnDuenio(duenio);

            mascotaRepository.save(mascota);
            return ResponseEntity.ok("Mascota modificada");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMascota(@PathVariable int id) {
        if (mascotaRepository.existsById(id)) {
            mascotaRepository.deleteById(id);
            return ResponseEntity.ok("Mascota eliminada");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


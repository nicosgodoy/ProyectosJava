package com.peluqueriacanina.Springpeluqueriacanina.repository;

import com.peluqueriacanina.Springpeluqueriacanina.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
}

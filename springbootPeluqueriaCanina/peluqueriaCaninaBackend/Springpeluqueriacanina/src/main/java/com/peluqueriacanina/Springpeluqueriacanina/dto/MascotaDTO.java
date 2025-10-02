package com.peluqueriacanina.Springpeluqueriacanina.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {

    private int id;
    private String nombreMascota;
    private String raza;
    private String color;
    private String observaciones;
    private String nombreDuenio;
    private String celDuenio;
    private String alergico;
    private String atencionEspecial;
}

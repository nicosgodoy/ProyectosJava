package com.peluqueriacanina.Springpeluqueriacanina.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;


    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Mascota  {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int num_cliente;
        private String nombre;
        private String raza;
        private String color;
        private String alergico;
        private String atencion_especial;
        private String observaciones;
        @OneToOne
        @JoinColumn(name = "id_duenio")
        private Duenio unDuenio;

    }





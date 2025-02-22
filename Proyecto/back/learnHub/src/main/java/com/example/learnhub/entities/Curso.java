package com.example.learnhub.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {
    String id;
    String nombre;
    String area;
    String link;
    String descripcion;
    float calificacion;
}

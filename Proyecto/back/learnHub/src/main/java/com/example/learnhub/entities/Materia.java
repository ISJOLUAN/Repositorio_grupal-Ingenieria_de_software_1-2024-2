package com.example.learnhub.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Materia {
    private String codigo; // ID único de la materia
    private String nombre;
    private int creditos;
    private String facultad;
    private int cupos;
    private String descripcion;
    private List<GrupoMateria> grupos; // Lista de grupos asociados a la materia
}

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
public class Canal {
    private String id;
    private String nombre;
    private String area;
    private int capacity;
    private int currentSize;
    private String descripcion;
    private String administrador;
    private List<String> miembros;
}

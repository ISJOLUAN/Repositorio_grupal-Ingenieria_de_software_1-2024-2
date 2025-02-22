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
public class GrupoMateria {

    private int numero; // Número del grupo
    private String horario; // Horario del grupo (ejemplo: "08:00-10:00")
    private List<String> dias; // Lista de días (siempre dos días)
    private String profesor; // Nombre del profesor
    private String salon; // Salón de clase
}

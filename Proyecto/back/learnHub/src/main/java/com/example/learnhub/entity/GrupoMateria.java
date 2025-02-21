package com.example.learnhub.entity;


import java.util.List;

public class GrupoMateria {

    private int numero; // Número del grupo
    private String horario; // Horario del grupo (ejemplo: "08:00-10:00")
    private List<String> dias; // Lista de días (siempre dos días)
    private String profesor; // Nombre del profesor
    private String salon; // Salón de clase

    public int getNumero() {
        return numero;
    }

    public String getHorario() {
        return horario;
    }

    public List<String> getDias() {
        return dias;
    }

    public String getSalon() {
        return salon;
    }

    public String getProfesor() {
        return profesor;
    }
}


package com.example.learnhub.entity;


import java.util.List;

public class Materia {

    private String codigo; // ID único de la materia
    private String nombre;
    private int creditos;
    private String facultad;
    private int cupos;
    private String descripcion;
    private List<GrupoMateria> grupos; // Lista de grupos asociados a la materia

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public String getFacultad() {
        return facultad;
    }

    public int getCupos() {
        return cupos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<GrupoMateria> getGrupos() {
        return grupos;
    }
}
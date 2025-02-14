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

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<GrupoMateria> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<GrupoMateria> grupos) {
        this.grupos = grupos;
    }
}
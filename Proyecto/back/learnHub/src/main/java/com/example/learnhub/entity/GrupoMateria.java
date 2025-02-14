package com.example.learnhub.entity;



import java.util.List;

public class GrupoMateria {

    private int numero; // Número del grupo
    private String horario; // Horario del grupo (ejemplo: "08:00-10:00")
    private List<String> dias; // Lista de días (siempre dos días)
    private String profesor; // Nombre del profesor
    private String salon; // Salón de clase

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }
}


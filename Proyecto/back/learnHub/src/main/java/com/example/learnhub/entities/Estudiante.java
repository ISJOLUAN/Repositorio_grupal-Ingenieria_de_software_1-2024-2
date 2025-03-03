package com.example.learnhub.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante {
    private String hash;
    private String correo;
    private String give_name;
    private String family_name;
    private String picture;
    public String getNombreCompleto() {
        return (give_name != null ? give_name : "") + " " + (family_name != null ? family_name : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estudiante that = (Estudiante) obj;
        return Objects.equals(this.correo, that.correo);
    }
}

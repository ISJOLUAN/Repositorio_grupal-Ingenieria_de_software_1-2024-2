package com.example.learnhub.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}

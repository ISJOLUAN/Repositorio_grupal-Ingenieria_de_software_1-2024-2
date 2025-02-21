package com.example.learnhub.utils;

import java.text.Normalizer;

public class StringUtils {

    public static String removeTildes(String input) {
        if (input == null) {
            return "";
        }
        // Normaliza la cadena para descomponer los caracteres con tildes
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Elimina los caracteres diacríticos (tildes)
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
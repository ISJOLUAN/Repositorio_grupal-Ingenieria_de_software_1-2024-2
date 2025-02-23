package com.example.learnhub.controllers;

import com.example.learnhub.entities.Materia;
import com.example.learnhub.services.MateriaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/materias")
@AllArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;


    @GetMapping("/buscar")
    public List<Materia> buscarMaterias(@RequestParam String q) {
        try {
            return materiaService.buscarPorCoincidencia(q);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error al buscar materias", e);
        }
    }

    @PostMapping("/newMateria")
    public String createMateria(@RequestBody Materia materia) throws ExecutionException, InterruptedException {
        return materiaService.saveMateria(materia);
    }

    @PostMapping("/materias/batch")
    public String saveMaterias(@RequestBody List<Materia> materias) throws ExecutionException, InterruptedException {
        return materiaService.saveMaterias(materias);
    }

}
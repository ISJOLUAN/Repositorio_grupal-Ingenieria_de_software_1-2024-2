package com.example.learnhub.controllers;


import com.example.learnhub.entity.Materia;
import com.example.learnhub.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {


    @Autowired
    private MateriaService materiaService;

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

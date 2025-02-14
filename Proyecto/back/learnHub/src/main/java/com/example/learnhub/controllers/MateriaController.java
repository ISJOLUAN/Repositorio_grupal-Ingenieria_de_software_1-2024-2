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
    private MateriaService productService;

    @Autowired
    private MateriaService materiaService;

    @GetMapping("/{codigo}")
    public Materia getMateria(@PathVariable String codigo) throws ExecutionException, InterruptedException {
        return materiaService.getMateriaByCodigo(codigo);
    }

    @GetMapping("/search")
    public List<Materia> searchMaterias(@RequestParam String query) throws ExecutionException, InterruptedException {
        return materiaService.searchMaterias(query);
    }

    @PostMapping("/newMateria")
    public String createMateria(@RequestBody Materia materia) throws ExecutionException, InterruptedException {
        return productService.saveMateria(materia);
    }

    @PostMapping("/materias/batch")
    public String saveMaterias(@RequestBody List<Materia> materias) throws ExecutionException, InterruptedException {
        return productService.saveMaterias(materias);
    }

}

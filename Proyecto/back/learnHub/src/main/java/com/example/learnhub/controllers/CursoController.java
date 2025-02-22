package com.example.learnhub.controllers;
import com.example.learnhub.entities.Curso;
import com.example.learnhub.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;
    @GetMapping("/buscar")
    public List<Curso> buscarMaterias(@RequestParam String q) {
        try {
            return cursoService.buscarPorCoincidencia(q);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error al buscar materias", e);
        }
    }
    @PostMapping("/newCurso")
    public String createCurso(@RequestBody Curso curso) throws ExecutionException, InterruptedException {
        return cursoService.saveCurso(curso);
    }
}
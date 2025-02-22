package com.example.learnhub.controllers;

import com.example.learnhub.entities.Canal;
import com.example.learnhub.services.CanalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/canales")
public class CanalController {

    private final CanalService canalService;

    public CanalController(CanalService canalService) {
        this.canalService = canalService;
    }

    @GetMapping("/search")
    public List<Canal> buscarCanal(@RequestParam String q) {
        try {
            return canalService.buscarPorCoincidencia(q);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error al buscar canales", e);
        }
    }
    @PostMapping("/newCanal")
    public String createCanal(@RequestBody Canal canal) throws ExecutionException, InterruptedException {
        return canalService.saveCanal(canal);
    }
}
package com.example.learnhub.controllers;


import com.example.learnhub.entity.Canal;
import com.example.learnhub.service.CanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/canales")
public class CanalController {
    @Autowired
    private CanalService canalService;
    @GetMapping("/buscar")
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

package com.example.learnhub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// controller de prueba para
@RestController
@RequestMapping("/hola")
public class HomeController {
    @GetMapping("/get")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hola");
    }

}

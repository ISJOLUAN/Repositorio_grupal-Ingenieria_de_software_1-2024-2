package com.example.learnhub.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }
    @GetMapping(value = "/{name}")
    public String get(@PathVariable String name) {
        return "Hello World" + name;
    }
}

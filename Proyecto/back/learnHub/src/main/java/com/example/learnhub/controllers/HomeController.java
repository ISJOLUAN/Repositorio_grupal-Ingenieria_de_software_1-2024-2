package com.example.learnhub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/learnhub")
    public String redirectToIndex() {
        return "forward:index.html";
    }
}
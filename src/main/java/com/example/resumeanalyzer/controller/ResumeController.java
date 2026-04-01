package com.example.resumeanalyzer.controller;

import com.example.resumeanalyzer.service.GroqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResumeController {

    @Autowired
    private GroqService groqService;

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }
}
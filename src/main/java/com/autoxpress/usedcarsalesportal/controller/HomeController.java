package com.autoxpress.usedcarsalesportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
	
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    
    @GetMapping("/register")
    public String redirectToRegister() {
        return "redirect:/user/register";
    }
}
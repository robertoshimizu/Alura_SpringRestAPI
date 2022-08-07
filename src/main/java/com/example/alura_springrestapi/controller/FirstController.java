package com.example.alura_springrestapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {
    @RequestMapping("/")
    @ResponseBody // Otherwise it would look for a JSP/HTML page
    public String welcome(){
        return "Welcome to RestAPI Starter";
    }
}

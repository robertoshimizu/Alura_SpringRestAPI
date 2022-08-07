package com.example.alura_springrestapi.controller;

import com.example.alura_springrestapi.controller.dto.TopicoDTO;
import com.example.alura_springrestapi.model.Curso;
import com.example.alura_springrestapi.model.Topico;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TopicosController {
    @RequestMapping("/topicos")
    public List<TopicoDTO> lista() {
        Topico topico = new Topico("Primeiros Passos Spring Boot", "Curso introdutório de Spring", new Curso("Spring101", "Java I"));
        return TopicoDTO.converter(Arrays.asList(topico, topico, topico));
    }
}

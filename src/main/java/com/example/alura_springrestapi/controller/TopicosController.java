package com.example.alura_springrestapi.controller;

import com.example.alura_springrestapi.controller.dto.TopicoDTO;
import com.example.alura_springrestapi.model.Curso;
import com.example.alura_springrestapi.model.Topico;
import com.example.alura_springrestapi.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @RequestMapping("/topicos")
    public List<TopicoDTO> lista() {

       List<Topico> topicos = topicoRepository.findAll();
        return TopicoDTO.converter(topicos);
    }
}

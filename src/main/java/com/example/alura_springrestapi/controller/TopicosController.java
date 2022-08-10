package com.example.alura_springrestapi.controller;

import com.example.alura_springrestapi.controller.dto.TopicoDTO;
import com.example.alura_springrestapi.controller.form.TopicoForm;
import com.example.alura_springrestapi.model.Curso;
import com.example.alura_springrestapi.model.Topico;
import com.example.alura_springrestapi.repository.CursoRepository;
import com.example.alura_springrestapi.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso) {
        if (nomeCurso==null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDTO.converter(topicos);
        }
        else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    public void cadastrar(@RequestBody TopicoForm form){

    //  Need to convert TopicoForm to Topico
    // For that we encapsulate conversion as method in TopicoForm
    // TopicoForm only has the nomeCurso, but Topico needs a Curso object
    // Therefore we create a Curso Repository and injects in the converter
    // so the implementation of method converter can query nomeCurso and return the Curso object
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

    }
}

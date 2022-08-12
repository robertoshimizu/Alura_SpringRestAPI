package com.example.alura_springrestapi.controller.dto;

import com.example.alura_springrestapi.model.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TopicoDetalhadoDTO {

    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private StatusTopico status;
    private Usuario autor;
    private Curso curso;
    private List<Resposta> respostas;

    public TopicoDetalhadoDTO(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.dataCriacao = topico.getDataCriacao();
        this.curso= topico.getCurso();
        this.status=topico.getStatus();
        this.autor = topico.getAutor();
        this.respostas = topico.getRespostas();
        //this.respostas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<RespostaDto> getRespostas() {

        List<RespostaDto> respostasDto = new ArrayList();
        this.respostas.forEach(resposta -> {
            RespostaDto respostaDto = new RespostaDto(resposta);
            respostasDto.add(respostaDto);
        });
        return respostasDto;
    }
}

package com.example.alura_springrestapi.controller.form;

import com.example.alura_springrestapi.model.Topico;
import com.example.alura_springrestapi.repository.TopicoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AtualizacaoTopicoForm {
    @NotNull
    @NotEmpty
    @Length(min = 5, max = 50)
    private String titulo;
    @NotNull @NotEmpty @Length(min = 5, max = 300)
    private String menssagem;

    public AtualizacaoTopicoForm(String titulo, String message) {
        this.titulo = titulo;
        this.menssagem = message;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMessage() {
        return menssagem;
    }

    public void setMessage(String message) {
        this.menssagem = message;
    }

    public Topico atualizar(Long id, TopicoRepository topicoRepository) {

        Topico topico = topicoRepository.getReferenceById(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.menssagem);

        return topico;
    }
}

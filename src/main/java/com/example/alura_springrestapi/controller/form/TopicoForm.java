package com.example.alura_springrestapi.controller.form;

import com.example.alura_springrestapi.model.Curso;
import com.example.alura_springrestapi.model.Topico;
import com.example.alura_springrestapi.repository.CursoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TopicoForm {
    @NotNull @NotEmpty @Length(min = 5, max = 50)
    private String titulo;
    @NotNull @NotEmpty @Length(min = 5, max = 300)
    private String message;
    @NotNull @NotEmpty @Length(min = 5, max = 30)
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository repo) {
        // we use the CursoRepository to instatiate a Curso object
        // by querying in the database the one that matches nomeCurso.
        Curso curso = repo.findByNome(nomeCurso);
        // We need to create a second constructor for the class Topico
        // remember, Topico is a POJO.
        return new Topico(titulo, message, curso);
    }
}

package com.example.alura_springrestapi.repository;

import com.example.alura_springrestapi.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico,Long> {
    List<Topico> findByCurso_Nome(String nomeCurso);
}

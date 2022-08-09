package com.example.alura_springrestapi.repository;

import com.example.alura_springrestapi.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico,Long> {
}

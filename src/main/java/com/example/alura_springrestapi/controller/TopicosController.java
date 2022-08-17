package com.example.alura_springrestapi.controller;

import com.example.alura_springrestapi.controller.dto.TopicoDTO;
import com.example.alura_springrestapi.controller.dto.TopicoDetalhadoDTO;
import com.example.alura_springrestapi.controller.form.AtualizacaoTopicoForm;
import com.example.alura_springrestapi.controller.form.TopicoForm;

import com.example.alura_springrestapi.model.Topico;
import com.example.alura_springrestapi.repository.CursoRepository;
import com.example.alura_springrestapi.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDTO> listaTopicos(@RequestParam(required = false) String nomeCurso,
                                        @PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0,size = 10)
                                        Pageable paginacao) {

        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
        //  Need to convert TopicoForm to Topico
        // For that we encapsulate conversion as method in TopicoForm
        // TopicoForm only has the nomeCurso, but Topico needs a Curso object
        // Therefore we create a Curso Repository and injects in the converter
        // so the implementation of method converter can query nomeCurso and return the Curso object
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TopicoDetalhadoDTO> oneTopico(@PathVariable("id") Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            TopicoDetalhadoDTO dto = new TopicoDetalhadoDTO(topico.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> update(@PathVariable("id") Long id,@RequestBody @Valid AtualizacaoTopicoForm form) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDTO(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<?> deleteTopic(@PathVariable("id") Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

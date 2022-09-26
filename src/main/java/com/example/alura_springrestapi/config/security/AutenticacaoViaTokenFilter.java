package com.example.alura_springrestapi.config.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

//    Nao existe anotacao tipo @Autowired, deve se registrar esta classe no Security Configurations

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     // pega o token da requisicao e usa o metodo abaixo para pegar a parte que interessa
        String token = recuperarToken(request);
        System.out.println(token);
        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer")){
            return  null;
        }
        return token.substring(7, token.length());
    }
}

package com.example.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repositories.UsuarioRepository;

@Controller	  
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Obtém a autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  // Redireciona para login se não estiver autenticado
        }

        String email = authentication.getName(); // Obtém o email do usuário autenticado

        usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {
            model.addAttribute("nomeUsuario", usuario.getNome()); // Adiciona o nome ao modelo
            model.addAttribute("emailUsuario", usuario.getEmail());
        }, () -> {
            model.addAttribute("nomeUsuario", "Usuário Desconhecido"); // Valor padrão se o usuário não for encontrado
        });

        return "tarefas"; // Retorna o template dashboard.html
    }
    
    
}

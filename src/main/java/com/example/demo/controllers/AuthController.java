package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Usuario;

import com.example.demo.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(
        @RequestParam(value = "sucesso", required = false) boolean sucesso,
        Model model
    ) {
        if (sucesso) {
            model.addAttribute("sucesso", "Conta criada com sucesso!");
        }
        return "login"; 
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro"; 
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody Usuario usuarioDTO) {
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@Valid Usuario usuarioDTO, Model model) {
        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            model.addAttribute("erro", "Este email já está cadastrado.");
            return "cadastro";
        }

        // Cria um novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha())); // Criptografa a senha

        // Salva o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Adiciona mensagem de sucesso ao modelo
        model.addAttribute("sucesso", "Conta criada com sucesso! <a href='/login'>Fazer Login</a>.");
        //retorna para cadastro.html
        return "cadastro";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenha() {
        return "recuperar-senha";
    }

    @PostMapping("/recuperar-senha")
    public String recuperarSenha(@RequestParam String email, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null) {
            model.addAttribute("mensagem", "Um email foi enviado para " + email);
        } else {
            model.addAttribute("mensagem", "Email não encontrado");
        }
        return "recuperar-senha";
    }

    // Captura erros de validação e retorna mensagem de erro para o frontend
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        ex.getBindingResult().getFieldErrors().forEach(error ->
            model.addAttribute("erro", error.getDefaultMessage())
        );
        return "cadastro"; 
    }
}
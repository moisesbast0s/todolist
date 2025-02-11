package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        // Verifica se o parâmetro "sucesso" está presente
        if (sucesso) {
            model.addAttribute("sucesso", "Conta criada com sucesso!");
        }

        return "login"; // Retorna o template login.html
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro"; // Retorna o template cadastro.html
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(
        @RequestParam String nome,
        @RequestParam String email,
        @RequestParam String senha,
        Model model // Usa Model para passar mensagens
    ) {
        // Verifica se o email já está cadastrado
        if (usuarioRepository.findByEmail(email).isPresent()) {
            model.addAttribute("erro", "Este email já está cadastrado.");
            return "cadastro"; // Retorna para a página de cadastro com a mensagem de erro
        }

        // Cria um novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha)); // Criptografa a senha

        // Salva o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Adiciona uma mensagem de sucesso ao modelo
        String mensagemSucesso = "Conta criada com sucesso! <a href='/login'>Fazer Login</a>.";
        model.addAttribute("sucesso", mensagemSucesso);

        // Retorna para a página de cadastro com a mensagem de sucesso
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
            // Lógica para enviar email de recuperação de senha
            model.addAttribute("mensagem", "Um email foi enviado para " + email);
        } else {
            model.addAttribute("mensagem", "Email não encontrado");
        }
        return "recuperar-senha";
    }
}
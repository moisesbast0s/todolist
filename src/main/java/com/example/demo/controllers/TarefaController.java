package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.models.Tarefa;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.TarefaRepository;
import com.example.demo.repositories.UsuarioRepository;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;	

    public TarefaController(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listarTarefas(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (usuario != null) {
            List<Tarefa> tarefas = tarefaRepository.findByUsuarioOrderByIdDesc(usuario); // Ordena pela mais recente primeiro
            
            model.addAttribute("tarefas", tarefas);
            model.addAttribute("nomeUsuario", usuario.getNome());  // Adiciona o nome do usuário ao modelo
        }
        return "dashboard";  // Certifique-se de que "dashboard.html" está correto
    }

    @PostMapping("/adicionar")
    public String adicionarTarefa(@RequestParam String descricao, @RequestParam String data, 
                                  @AuthenticationPrincipal UserDetails userDetails) {
    	
        System.out.println("Tentando adicionar tarefa: " + descricao + " - " + data);

        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (usuario != null) {
            Tarefa tarefa = new Tarefa();
            tarefa.setDescricao(descricao);
            tarefa.setData(LocalDate.parse(data));
            tarefa.setConcluida(false);
            tarefa.setUsuario(usuario);
            tarefaRepository.save(tarefa);
        }
        return "redirect:/tarefas";  // Redireciona para atualizar a lista
    }
    
    
    @PostMapping("/concluir/{id}")
    public String concluirTarefa(@PathVariable Long id, @RequestParam(required = false) boolean concluida) {
        Tarefa tarefa = tarefaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        // Se o parâmetro 'concluida' for enviado, use-o; caso contrário, marque como true
        tarefa.setConcluida(concluida);
        tarefaRepository.save(tarefa);

        // Redireciona para a mesma página (por exemplo, o dashboard ou a página de tarefas)
        return "redirect:/dashboard"; // ou "redirect:/tarefas", conforme sua rota
    }


    // Método para excluir a tarefa
    @PostMapping("/remover/{id}")
    public String removerTarefa(@PathVariable Long id) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa != null) {
            tarefaRepository.delete(tarefa);
        }
        return "redirect:/tarefas";  // Redireciona de volta ao dashboard ou página de tarefas
    }
}

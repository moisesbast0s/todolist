package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    
    @PostMapping("/tarefas/concluir/{id}")
    @ResponseBody
    public ResponseEntity<String> concluirTarefa(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        System.out.println("Recebendo requisição para concluir tarefa ID: " + id);
        
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            System.out.println("Status antes: " + tarefa.isConcluida());
            
            tarefa.setConcluida(payload.get("concluida")); // Atualiza o status da tarefa
            tarefaRepository.save(tarefa);

            System.out.println("Status depois: " + tarefa.isConcluida());
            return ResponseEntity.ok("Tarefa atualizada com sucesso");
        }
        
        System.err.println("Tarefa não encontrada para o ID: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
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

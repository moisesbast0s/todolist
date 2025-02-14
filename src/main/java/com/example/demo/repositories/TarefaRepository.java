package com.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Tarefa;
import com.example.demo.models.Usuario;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByUsuario(Usuario usuario);
    List<Tarefa> findByUsuarioOrderByIdDesc(Usuario usuario); // Novo método com ordenação

}

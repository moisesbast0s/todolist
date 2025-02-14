package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private LocalDate data;
    @Column(nullable = false)
    private boolean concluida; // Deve ser boolean
   

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

	
  
    
}

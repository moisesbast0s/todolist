package com.example.demo;

	import jakarta.validation.constraints.Email;
	import jakarta.validation.constraints.NotBlank;
	import jakarta.validation.constraints.Size;

	public class UsuarioValid{
	    
	    @NotBlank(message = "O nome não pode estar vazio")
	    private String nome;

	    @Email(message = "Email inválido")
	    @NotBlank(message = "O email é obrigatório")
	    private String email;

	    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
	    private String senha;

	    
	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getSenha() {
	        return senha;
	    }

	    public void setSenha(String senha) {
	        this.senha = senha;
	    }
	}



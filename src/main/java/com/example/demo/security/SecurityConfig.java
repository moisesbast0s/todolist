package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/cadastro", "/recuperar-senha", "/public/**", "css/styles.css", "morcego.webp", "teste.jpg").permitAll() // Rotas públicas   
                .requestMatchers("/tarefas/**", "/tarefas/concluir/**", "/tarefas/remover/**").authenticated()  // Apenas usuários autenticados podem acessar tarefas
                .anyRequest().authenticated() // Todas as outras rotas exigem autenticação
            )
            .formLogin((form) -> form
                .loginPage("/login") // Página de login personalizada
                .defaultSuccessUrl("/tarefas", true) // Redireciona para /dashboard após o login
                .failureUrl("/login?error=true") // Redireciona em caso de falha
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login") // Redireciona para /login após o logout
                .permitAll()
            );

        return http.build();	
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
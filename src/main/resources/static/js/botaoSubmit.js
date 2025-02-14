function submitConcluir(checkbox) {
    // Impede o comportamento padrão (envio do formulário)
    event.preventDefault();
    
    // Seleciona o formulário mais próximo
    var form = checkbox.closest('form');
    
    // Cria uma requisição AJAX usando fetch
    fetch(form.action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
            // Se você estiver usando CSRF, inclua o token aqui
        },
        body: new URLSearchParams(new FormData(form))
    })
    .then(response => {
        if(response.ok) {
            // Recarrega a página para refletir as alterações
            window.location.reload();
        } else {
            console.error('Erro ao concluir tarefa.');
        }
    })
    .catch(error => console.error('Erro:', error));
}
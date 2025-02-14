$(document).ready(function() {
     $('.concluir-btn').click(function(event) {
         event.preventDefault(); // Impede o comportamento padrão (submit do formulário)
         
         const tarefaId = $(this).data('tarefa-id');
         const botao = $(this); // Guarda referência ao botão clicado

         $.ajax({
             url: '/concluir/' + tarefaId,
             method: 'POST',
             success: function() {
                 // Atualiza visualmente a tarefa sem recarregar a página
                 botao.closest('li').addClass('concluida'); // Adiciona classe CSS
                 botao.prop('disabled', true); // Desabilita o botão
             },
             error: function(xhr, status, error) {
                 alert('Erro ao concluir tarefa: ' + error);
             }
         });
     });
 });
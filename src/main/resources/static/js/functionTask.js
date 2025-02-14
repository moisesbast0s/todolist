$(document).ready(function(){
    $('.concluir-form').on('submit', function(e){
        e.preventDefault(); // Impede o envio tradicional do formulário

        var form = $(this);
        var concluida = form.find("input[name='concluida']").is(':checked');
        var tarefaId = form.find("input[name='id']").val();

        var dataToSend = {
            id: tarefaId,
            concluida: concluida
        };

        $.ajax({
            url: form.attr('action'),
            method: form.attr('method'),
            data: dataToSend,
            success: function(){
                // Recarrega a página para refletir a mudança
                location.reload();
            },
            error: function(xhr, status, error){
                console.error('Erro ao atualizar a tarefa:', error);
                // Apenas exibe no console ao invés de um pop-up
            }
        });
    });

    // Submete o formulário ao marcar/desmarcar a checkbox
    $('.concluir-form input[type="checkbox"]').on('change', function(){
        $(this).closest('.concluir-form').submit();
    });
});
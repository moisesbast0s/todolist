document.addEventListener("DOMContentLoaded", function() {
        flatpickr("#datepicker", {
            dateFormat: "Y-m-d",  // Formato AAAA-MM-DD para o banco
            altInput: true,
            altFormat: "d/m/Y",   // Formato visível ao usuário
            locale: "pt",
			minDate: "today",
			allowInput: true, // prevent "readonly" prop
        });
    });
// Validação do formulário
(function() {
	'use strict';
	window.addEventListener('load', function() {
		var forms = document.getElementsByClassName('needs-validation');
		var validation = Array.prototype.filter.call(forms, function(form) {
			form.addEventListener('submit', function(event) {
				if (form.checkValidity() === false) {
					event.preventDefault();
					event.stopPropagation();
				}
				form.classList.add('was-validated');
			}, false);
		});
	}, false);
})();
// Funcionamento do Sidebar
$(document).ready(function() {
	$("#sidebar").mCustomScrollbar({
		theme : "minimal"
	});
	$('#sidebarCollapse').on('click', function() {
		$('#sidebar,#sidebarCollapse').toggleClass('active');
		$('.collapse.show').toggleClass('show');
		$('a[aria-expanded=true]').attr('aria-expanded', 'false');
	});
	if ($('header').width() < 768 ){
		$('#sidebarCollapse').click();
 }
});
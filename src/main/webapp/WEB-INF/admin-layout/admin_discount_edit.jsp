<%@page import="main.java.entities.Discount"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
	// muestra un número con hasta 2 decimales y punto como separador; evita arrastrar la imprecisión del float de la base.
	private static String formatNumber(double value) {
		return new java.text.DecimalFormat("0.##", new java.text.DecimalFormatSymbols(java.util.Locale.US)).format(value);
	}
%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	Discount discount = (Discount) request.getAttribute("discount");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Editar Descuento</title>
	<link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
	<link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
	<link rel="stylesheet" href="styles/admin_panel.css" />
	<link rel="icon" type="ico" href="assets/icon2.ico" />
</head>
<body class="admin-page">
	<div class="admin-layout">
		<%@ include file="admin_header.jsp" %>

		<main class="admin-content">
			<section class="admin-panel" aria-label="Editar descuento">
				<h1>Editar descuento</h1>

				<form action="DiscountEdit" method="post" class="admin-form">
					<input type="hidden" name="discount_id" value="<%= discount.getDiscount_id() %>" />

					<label for="minimum_amount">Monto mínimo del pedido</label>
					<input type="text" id="minimum_amount" name="minimum_amount" inputmode="decimal" value="<%= formatNumber(discount.getMinimum_amount()) %>" required />

					<label for="discount_percentage">Porcentaje de descuento (%)</label>
					<input type="text" id="discount_percentage" name="discount_percentage" inputmode="decimal" value="<%= formatNumber(discount.getDiscount_percentage() * 100) %>" required />

					<button type="submit" class="admin-submit">Guardar cambios</button>
				</form>

				<a href="AdminDiscounts" class="admin-cancel-link">Cancelar</a>
			</section>
		</main>
	</div>

	<script>
		document.addEventListener('DOMContentLoaded', () => {
			// reemplaza el globo nativo de "completá este campo" por un mensaje con el estilo de la app
			document.querySelectorAll('form.admin-form').forEach((form) => {
				form.setAttribute('novalidate', 'novalidate');
				form.addEventListener('submit', (e) => {
					if (!form.checkValidity()) {
						e.preventDefault();
						let errorEl = form.querySelector('.admin-form__error');
						if (!errorEl) {
							errorEl = document.createElement('p');
							errorEl.className = 'admin-message admin-form__error';
							form.prepend(errorEl);
						}
						errorEl.textContent = 'Completá todos los campos obligatorios.';
					}
				});
			});
		});
	</script>
</body>
</html>

<%@page import="java.util.LinkedList"%>
<%@page import="main.java.entities.Discount"%>
<%@page import="main.java.entities.User"%>
<%@page import="main.java.logic.HtmlUtils"%>
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
	String message = (String) request.getAttribute("message");
	LinkedList<Discount> discounts = (LinkedList<Discount>) request.getAttribute("discounts");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Gestionar Descuentos</title>
	<link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
	<link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
	<link rel="stylesheet" href="styles/admin_panel.css" />
	<link rel="icon" type="ico" href="assets/icon2.ico" />
</head>
<body class="admin-page">
	<div class="admin-layout">
		<%@ include file="admin_header.jsp" %>

		<main class="admin-content admin-content--wide">
			<section class="admin-panel" aria-label="Descuentos existentes">
				<div class="admin-section-header">
					<h1>Descuentos</h1>
					<button type="button" id="openCreateDiscount" class="admin-submit admin-submit--small">Crear descuento</button>
				</div>

				<% if (message != null) { %>
					<p class="admin-message"><%= HtmlUtils.escape(message) %></p>
				<% } %>

				<% if (discounts != null && !discounts.isEmpty()) { %>
					<ul class="admin-product-list">
						<% for (Discount discount : discounts) { %>
							<li class="admin-product-item">
								<div class="admin-product-item__info">
									<strong><%= formatNumber(discount.getDiscount_percentage() * 100) %>% de descuento</strong>
									<p>En pedidos desde $<%= formatNumber(discount.getMinimum_amount()) %></p>
								</div>
								<div class="admin-product-item__actions">
									<a href="DiscountEdit?id=<%= discount.getDiscount_id() %>" class="admin-action-link">Editar</a>
									<form action="DiscountDelete" method="post" data-confirm="¿Eliminar este descuento?">
										<input type="hidden" name="discount_id" value="<%= discount.getDiscount_id() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">Todavía no hay descuentos cargados.</p>
				<% } %>
			</section>
		</main>

		<dialog id="createDiscountModal" class="admin-modal">
			<div class="admin-modal__content">
				<button type="button" id="closeCreateDiscount" class="admin-modal__close" aria-label="Cerrar">&times;</button>
				<h2>Crear descuento</h2>

				<form action="DiscountCreate" method="post" class="admin-form">
					<label for="minimum_amount">Monto mínimo del pedido</label>
					<input type="text" id="minimum_amount" name="minimum_amount" inputmode="decimal" required />

					<label for="discount_percentage">Porcentaje de descuento (%)</label>
					<input type="text" id="discount_percentage" name="discount_percentage" inputmode="decimal" required />

					<button type="submit" class="admin-submit">Crear descuento</button>
				</form>
			</div>
		</dialog>

		<dialog id="confirmDeleteModal" class="admin-modal">
			<div class="admin-modal__content">
				<h2>Confirmar</h2>
				<p id="confirmDeleteMessage"></p>
				<div class="admin-modal__actions">
					<button type="button" id="confirmDeleteCancel" class="admin-action-link">Cancelar</button>
					<button type="button" id="confirmDeleteAccept" class="admin-action-link admin-action-link--danger">Eliminar</button>
				</div>
			</div>
		</dialog>
	</div>

	<script>
		document.addEventListener('DOMContentLoaded', () => {
			const modal = document.getElementById('createDiscountModal');
			const openBtn = document.getElementById('openCreateDiscount');
			const closeBtn = document.getElementById('closeCreateDiscount');

			if (openBtn) {
				openBtn.addEventListener('click', () => modal.showModal());
			}
			if (closeBtn) {
				closeBtn.addEventListener('click', () => modal.close());
			}

			// modal de confirmación para los "Eliminar" (reemplaza el confirm() del navegador)
			const confirmModal = document.getElementById('confirmDeleteModal');
			const confirmMessage = document.getElementById('confirmDeleteMessage');
			const confirmAccept = document.getElementById('confirmDeleteAccept');
			const confirmCancel = document.getElementById('confirmDeleteCancel');
			let formPendingDelete = null;

			document.querySelectorAll('form[data-confirm]').forEach((form) => {
				form.addEventListener('submit', (e) => {
					e.preventDefault();
					formPendingDelete = form;
					confirmMessage.textContent = form.getAttribute('data-confirm');
					confirmModal.showModal();
				});
			});
			confirmAccept.addEventListener('click', () => {
				confirmModal.close();
				if (formPendingDelete) { formPendingDelete.submit(); }
			});
			confirmCancel.addEventListener('click', () => confirmModal.close());

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

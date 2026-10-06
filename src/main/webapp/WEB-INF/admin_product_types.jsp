<%@page import="java.util.LinkedList"%>
<%@page import="main.java.entities.ProductType"%>
<%@page import="main.java.entities.User"%>
<%@page import="main.java.logic.HtmlUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	String message = (String) request.getAttribute("message");
	LinkedList<ProductType> productTypes = (LinkedList<ProductType>) request.getAttribute("productTypes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Gestionar Tipos de Producto</title>
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
			<section class="admin-panel" aria-label="Tipos de producto existentes">
				<div class="admin-section-header">
					<h1>Tipos de producto</h1>
					<button type="button" id="openCreateProductType" class="admin-submit admin-submit--small">Crear tipo</button>
				</div>

				<% if (message != null) { %>
					<p class="admin-message"><%= HtmlUtils.escape(message) %></p>
				<% } %>

				<% if (productTypes != null && !productTypes.isEmpty()) { %>
					<ul class="admin-product-list">
						<% for (ProductType productType : productTypes) { %>
							<li class="admin-product-item">
								<div class="admin-product-item__info">
									<strong><%= HtmlUtils.escape(productType.getName()) %></strong>
								</div>
								<div class="admin-product-item__actions">
									<a href="ProductTypeEdit?id=<%= productType.getProduct_type_id() %>" class="admin-action-link">Editar</a>
									<form action="ProductTypeDelete" method="post" data-confirm="¿Eliminar este tipo de producto?">
										<input type="hidden" name="product_type_id" value="<%= productType.getProduct_type_id() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">Todavía no hay tipos de producto cargados.</p>
				<% } %>
			</section>
		</main>

		<dialog id="createProductTypeModal" class="admin-modal">
			<div class="admin-modal__content">
				<button type="button" id="closeCreateProductType" class="admin-modal__close" aria-label="Cerrar">&times;</button>
				<h2>Crear tipo de producto</h2>

				<form action="ProductTypeCreate" method="post" class="admin-form">
					<label for="name">Nombre</label>
					<input type="text" id="name" name="name" maxlength="80" required />

					<button type="submit" class="admin-submit">Crear tipo</button>
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
			const modal = document.getElementById('createProductTypeModal');
			const openBtn = document.getElementById('openCreateProductType');
			const closeBtn = document.getElementById('closeCreateProductType');

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

<%@page import="java.util.LinkedList"%>
<%@page import="main.java.entities.Product"%>
<%@page import="main.java.entities.ProductType"%>
<%@page import="main.java.entities.Restaurant"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	String message = (String) request.getAttribute("message");
	Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
	LinkedList<Product> products = (LinkedList<Product>) request.getAttribute("products");
	LinkedList<ProductType> productTypes = (LinkedList<ProductType>) request.getAttribute("productTypes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Gestionar Productos</title>
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
			<section class="admin-panel" aria-label="Productos del restaurante">
				<div class="admin-section-header">
					<h1>Productos de <%= restaurant.getName() %></h1>
					<% if (productTypes != null && !productTypes.isEmpty()) { %>
						<button type="button" id="openCreateProduct" class="admin-submit admin-submit--small">Crear producto</button>
					<% } %>
				</div>

				<% if (productTypes == null || productTypes.isEmpty()) { %>
					<p class="admin-message">Todavía no hay tipos de producto cargados en la base de datos; hace falta al menos uno para poder crear un producto.</p>
				<% } %>

				<% if (message != null) { %>
					<p class="admin-message"><%= message %></p>
				<% } %>

				<% if (products != null && !products.isEmpty()) { %>
					<ul class="admin-product-list">
						<% for (Product product : products) { %>
							<li class="admin-product-item">
								<div class="admin-product-item__info">
									<strong><%= product.getDescription() %></strong>
									<span class="admin-role-badge"><%= product.getProduct_type() != null ? product.getProduct_type().getName() : "Sin tipo" %></span>
									<p>$<%= product.getPrice() %></p>
								</div>
								<div class="admin-product-item__actions">
									<a href="ProductEdit?restaurant_id=<%= restaurant.getRestaurant_id() %>&product_id=<%= product.getProduct_id() %>" class="admin-action-link">Editar</a>
									<form action="ProductDelete" method="post" data-confirm="¿Eliminar este producto?">
										<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />
										<input type="hidden" name="product_id" value="<%= product.getProduct_id() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">Este restaurante todavía no tiene productos cargados.</p>
				<% } %>

				<a href="RestaurantEdit?id=<%= restaurant.getRestaurant_id() %>" class="admin-cancel-link">Volver al restaurante</a>
			</section>
		</main>

		<% if (productTypes != null && !productTypes.isEmpty()) { %>
		<dialog id="createProductModal" class="admin-modal">
			<div class="admin-modal__content">
				<button type="button" id="closeCreateProduct" class="admin-modal__close" aria-label="Cerrar">&times;</button>
				<h2>Crear producto</h2>

				<form action="ProductCreate" method="post" class="admin-form">
					<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />

					<label for="description">Descripción</label>
					<input type="text" id="description" name="description" required />

					<label for="price">Precio</label>
					<input type="number" id="price" name="price" min="0.01" step="0.01" required />

					<label for="product_type_id">Tipo de producto</label>
					<select id="product_type_id" name="product_type_id">
						<% for (ProductType productType : productTypes) { %>
							<option value="<%= productType.getProduct_type_id() %>"><%= productType.getName() %></option>
						<% } %>
					</select>

					<button type="submit" class="admin-submit">Crear producto</button>
				</form>
			</div>
		</dialog>
		<% } %>

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
			const modal = document.getElementById('createProductModal');
			const openBtn = document.getElementById('openCreateProduct');
			const closeBtn = document.getElementById('closeCreateProduct');

			if (openBtn && modal) {
				openBtn.addEventListener('click', () => modal.showModal());
			}
			if (closeBtn && modal) {
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

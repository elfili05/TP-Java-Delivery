<%@page import="java.util.LinkedList"%>
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
	String query = (String) request.getAttribute("query");
	LinkedList<Restaurant> restaurants = (LinkedList<Restaurant>) request.getAttribute("restaurants");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Gestionar Restaurantes</title>
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
			<section class="admin-panel" aria-label="Restaurantes existentes">
				<div class="admin-section-header">
					<h1>Restaurantes</h1>
					<button type="button" id="openCreateRestaurant" class="admin-submit admin-submit--small">Crear restaurante</button>
				</div>

				<form action="AdminRestaurants" method="get" class="admin-search">
					<input type="text" name="q" value="<%= query != null ? query : "" %>" placeholder="Buscar por nombre o dirección..." />
					<button type="submit" class="admin-action-link">Buscar</button>
				</form>

				<% if (restaurants != null && !restaurants.isEmpty()) { %>
					<ul class="admin-restaurant-list">
						<% for (Restaurant restaurant : restaurants) {
							String imgSrc = restaurant.getImage_url() != null ? restaurant.getImage_url() : "assets/icon2.ico";
						%>
							<li class="admin-restaurant-item">
								<img src="<%= imgSrc %>" alt="" class="admin-restaurant-item__img" />
								<div class="admin-restaurant-item__info">
									<strong><%= restaurant.getName() %></strong>
									<p><%= restaurant.getAddress() %></p>
								</div>
								<div class="admin-restaurant-item__actions">
									<a href="RestaurantEdit?id=<%= restaurant.getRestaurant_id() %>" class="admin-action-link">Editar</a>
									<form action="RestaurantDelete" method="post" data-confirm="¿Eliminar este restaurante?">
										<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">
						<%= (query != null && !query.trim().isEmpty()) ? "No se encontraron restaurantes para esa búsqueda." : "Todavía no hay restaurantes cargados." %>
					</p>
				<% } %>
			</section>
		</main>

		<dialog id="createRestaurantModal" class="admin-modal" <%= message != null ? "open" : "" %>>
			<div class="admin-modal__content">
				<button type="button" id="closeCreateRestaurant" class="admin-modal__close" aria-label="Cerrar">&times;</button>
				<h2>Crear restaurante</h2>

				<% if (message != null) { %>
					<p class="admin-message"><%= message %></p>
				<% } %>

				<form action="RestaurantCreate" method="post" class="admin-form">
					<label for="name">Nombre</label>
					<input type="text" id="name" name="name" required />

					<label for="address">Dirección</label>
					<input type="text" id="address" name="address" required />

					<label for="image_url">Imagen (ruta o URL)</label>
					<input type="text" id="image_url" name="image_url" placeholder="uploads/mi-restaurante.jpg" />

					<button type="submit" class="admin-submit">Crear restaurante</button>
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
			const modal = document.getElementById('createRestaurantModal');
			const openBtn = document.getElementById('openCreateRestaurant');
			const closeBtn = document.getElementById('closeCreateRestaurant');

			if (modal && modal.hasAttribute('open')) {
				modal.showModal();
			}

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

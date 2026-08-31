<%@page import="java.util.List"%>
<%@page import="main.java.data.RestaurantRepository"%>
<%@page import="main.java.entities.Restaurant"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Inicio</title>
	<link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
	<link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
	<link rel="stylesheet" href="styles/main_page.css" />
	<%
		User u = (User) session.getAttribute("user");
		String userName = (u != null && u.getName() != null && !u.getName().isBlank() && !"guest".equalsIgnoreCase(u.getRole())) ? u.getName() : "Invitado";
		String userAddress = (u != null && u.getAddress() != null && !u.getAddress().isBlank()) ? u.getAddress() : "Tu dirección";
		RestaurantRepository restaurantRepository = new RestaurantRepository();
		List<Restaurant> restaurants = restaurantRepository.getAll();
	%>
</head>
<body class="home-page">
	<div class="home-layout">
		<header class="topbar">
			<div class="brand-block" aria-label="Java Delivery logo">
				<img src="assets/icon2.ico" alt="Java Delivery logo"></img>
				<span class="brand-text">Java Delivery</span>
			</div>

			<div class="delivery-target">
				<span class="delivery-target__label">Enviar a:</span>
				<span class="delivery-target__value"><%= userAddress %></span>
			</div>

			<div class="user-welcome" aria-label="Usuario logueado">
				<div class="user-welcome__avatar"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6">
  <path fill-rule="evenodd" d="M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z" clip-rule="evenodd" />
</svg>
				</div>
				<span class="user-welcome__text">Hola, <strong><%= userName %></strong></span>
			</div>
		</header>

		<section class="restaurants-banner" aria-label="Banner de restaurantes disponibles">
			<div class="restaurants-banner__placeholder">[ Imagen de fondo ]</div>
			<h1>Restaurantes Disponibles</h1>
		</section>

		<section class="restaurant-section" aria-label="Listado de restaurantes">
			<div class="restaurant-list">
				<%
				if (restaurants != null && !restaurants.isEmpty()) {
					for (Restaurant restaurant : restaurants) {
						String name = restaurant.getName() != null ? restaurant.getName() : "Restaurante";
						String address = restaurant.getAddress() != null ? restaurant.getAddress() : "Dirección no disponible";
						int restaurantId = restaurant.getRestaurant_id();
					%>
						<a class="restaurant-card" href="menu.jsp?restaurantId=<%= restaurantId %>" aria-label="Ver menú de <%= name %>">
							<div class="restaurant-card__media" aria-hidden="true"></div>
							<div class="restaurant-card__content">
								<h2><%= name %></h2>
								<p><%= address %></p>
							</div>
						</a>
					<%
					}
				} else {
				%>
					<div class="restaurant-empty" aria-live="polite">
						<h2>Sin restaurantes</h2>
						<p>No hay restaurantes disponibles por el momento.</p>
					</div>
				<%
				}
				%>
			</div>
		</section>

		<footer class="bottom-bar">
			<span>Java Delivery</span>
			<span>— 2026</span>
		</footer>
	</div>
</body>
</html>
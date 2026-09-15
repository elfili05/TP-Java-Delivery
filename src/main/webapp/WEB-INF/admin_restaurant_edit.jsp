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
	Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Editar Restaurante</title>
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
			<section class="admin-panel" aria-label="Editar restaurante">
				<h1>Editar restaurante</h1>

				<form action="RestaurantEdit" method="post" class="admin-form">
					<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />

					<label for="name">Nombre</label>
					<input type="text" id="name" name="name" value="<%= restaurant.getName() %>" required />

					<label for="address">Dirección</label>
					<input type="text" id="address" name="address" value="<%= restaurant.getAddress() %>" required />

					<label for="image_url">Imagen (ruta o URL)</label>
					<input type="text" id="image_url" name="image_url" value="<%= restaurant.getImage_url() != null ? restaurant.getImage_url() : "" %>" placeholder="uploads/mi-restaurante.jpg" />

					<button type="submit" class="admin-submit">Guardar cambios</button>
				</form>

				<a href="AdminRestaurants" class="admin-cancel-link">Cancelar</a>
			</section>
		</main>
	</div>
</body>
</html>

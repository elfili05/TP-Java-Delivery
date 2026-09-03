<%@page import="java.util.LinkedList"%>
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
	LinkedList<User> users = (LinkedList<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Gestionar Usuarios</title>
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
			<section class="admin-panel" aria-label="Usuarios existentes">
				<div class="admin-section-header">
					<h1>Usuarios</h1>
				</div>

				<form action="AdminUsers" method="get" class="admin-search">
					<input type="text" name="q" value="<%= query != null ? query : "" %>" placeholder="Buscar por nombre, apellido o email..." />
					<button type="submit" class="admin-action-link">Buscar</button>
				</form>

				<% if (message != null) { %>
					<p class="admin-message"><%= message %></p>
				<% } %>

				<% if (users != null && !users.isEmpty()) { %>
					<ul class="admin-user-list">
						<% for (User user : users) { %>
							<li class="admin-user-item">
								<div class="admin-user-item__info">
									<strong><%= user.getName() %> <%= user.getSurname() %></strong>
									<span class="admin-role-badge <%= "admin".equalsIgnoreCase(user.getRole()) ? "admin-role-badge--admin" : "" %>"><%= user.getRole() %></span>
									<p><%= user.getEmail() %></p>
								</div>
								<div class="admin-user-item__actions">
									<a href="UserEdit?email=<%= user.getEmail() %>" class="admin-action-link">Editar</a>
									<form action="UserDelete" method="post" onsubmit="return confirm('¿Eliminar este usuario?');">
										<input type="hidden" name="email" value="<%= user.getEmail() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">
						<%= (query != null && !query.trim().isEmpty()) ? "No se encontraron usuarios para esa búsqueda." : "Todavía no hay usuarios cargados." %>
					</p>
				<% } %>
			</section>
		</main>
	</div>
</body>
</html>

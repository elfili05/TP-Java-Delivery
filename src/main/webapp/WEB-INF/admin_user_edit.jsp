<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	User editedUser = (User) request.getAttribute("editedUser");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Editar Usuario</title>
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
			<section class="admin-panel" aria-label="Editar usuario">
				<h1>Editar usuario</h1>

				<form action="UserEdit" method="post" class="admin-form">
					<input type="hidden" name="email" value="<%= editedUser.getEmail() %>" />

					<label>Email</label>
					<input type="text" value="<%= editedUser.getEmail() %>" disabled />

					<label for="name">Nombre</label>
					<input type="text" id="name" name="name" value="<%= editedUser.getName() != null ? editedUser.getName() : "" %>" required />

					<label for="surname">Apellido</label>
					<input type="text" id="surname" name="surname" value="<%= editedUser.getSurname() != null ? editedUser.getSurname() : "" %>" required />

					<label for="phone_number">Teléfono</label>
					<input type="text" id="phone_number" name="phone_number" value="<%= editedUser.getPhone_number() != null ? editedUser.getPhone_number() : "" %>" />

					<label for="dni">DNI</label>
					<input type="text" id="dni" name="dni" value="<%= editedUser.getDni() != null ? editedUser.getDni() : "" %>" />

					<label for="address">Dirección</label>
					<input type="text" id="address" name="address" value="<%= editedUser.getAddress() != null ? editedUser.getAddress() : "" %>" />

					<label for="role">Rol</label>
					<select id="role" name="role">
						<option value="client" <%= "client".equalsIgnoreCase(editedUser.getRole()) ? "selected" : "" %>>Cliente</option>
						<option value="admin" <%= "admin".equalsIgnoreCase(editedUser.getRole()) ? "selected" : "" %>>Administrador</option>
					</select>

					<button type="submit" class="admin-submit">Guardar cambios</button>
				</form>

				<a href="AdminUsers" class="admin-cancel-link">Cancelar</a>
			</section>
		</main>
	</div>
</body>
</html>

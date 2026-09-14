<%@ page import="main.java.entities.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Java Delivery | Orden confirmada</title>
<link rel="stylesheet" href="styles/main.css" />
<link rel="stylesheet" href="styles/main_page.css" />
</head>
<body class="page">
	<main class="signin-layout" aria-labelledby="confirmTitle">
		<section class="signin-panel">
			<div class="signin-brand" aria-hidden="true">
				<div class="signin-brand__placeholder">
					<img src="assets/logo2.png" alt="Java Delivery logo">
				</div>
			</div>

			<div class="signin-content">
				<%-- load user from session if available --%>
				<%
					User u = (User) session.getAttribute("user");
					String userName = (u != null && u.getName() != null && !u.getName().isBlank()) ? u.getName() : "Invitado";
					String userPhone = (u != null && u.getPhone_number() != null && !u.getPhone_number().isBlank()) ? u.getPhone_number() : "(sin teléfono)";
				%>

				<h1 id="confirmTitle" class="signin-title">¡Gracias por tu pedido!</h1>

				<p class="signin-error__message" style="margin-bottom:1.25rem;">Se ha registrado el pedido a nombre de <strong><%= userName %></strong>.<br>
				Serás notificado al nro. <strong><%= userPhone %></strong> cuando llegue a destino. ¡Gracias por elegirnos!</p>

				<div style="display:flex;gap:1rem;justify-content:center;width:100%;margin-top:0.5rem;">
					<form action="signin" method="post">
						<button type="submit" class="signin-form__button" style="min-width:220px;">Volver al menú</button>
					</form>

					<form action="logout" method="post">
						<button type="submit" class="signin-form__button" style="min-width:220px;">Cerrar sesión</button>
					</form>
				</div>
			</div>
		</section>
	</main>
</body>
</html>
<%@page import="main.java.entities.User"%>
<%@page import="main.java.logic.HtmlUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || u.getRole() == null || "guest".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	User profile = (User) request.getAttribute("profileUser");
	if (profile == null) {
		profile = u;
	}
	String message = (String) request.getAttribute("message");
	boolean isError = request.getAttribute("isError") != null;
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Mi perfil</title>
	<link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
	<link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700;800&display=swap" rel="stylesheet" />
	<link rel="stylesheet" href="styles/main.css" />
	<link rel="icon" type="ico" href="assets/icon2.ico" />
</head>

<body class="page profile-page">
	<main class="signin-layout signup-layout">

		<section class="signin-panel signup-panel" aria-labelledby="profile-title">
			<div class="signin-brand" aria-label="Java Delivery">
				<div class="signin-brand__placeholder">
					<img src="assets/logo2.png" alt="Java Delivery" />
				</div>
			</div>

			<div class="signin-content">
				<h1 class="signin-title" id="profile-title">Mi perfil</h1>

				<% if (message != null) { %>
					<p class="profile-message <%= isError ? "profile-message--error" : "profile-message--success" %>" role="<%= isError ? "alert" : "status" %>">
						<%= HtmlUtils.escape(message) %>
					</p>
				<% } %>

				<form class="signin-form signup-form" id="profileForm" action="UserProfile" method="post" novalidate>
					<div class="signin-form__field">
						<label class="profile-form__label" for="name">Nombre</label>
						<input id="name" class="signin-form__input" type="text" name="name" maxlength="80"
							value="<%= HtmlUtils.escape(profile.getName()) %>" autocomplete="given-name" required />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="surname">Apellido</label>
						<input id="surname" class="signin-form__input" type="text" name="surname" maxlength="80"
							value="<%= HtmlUtils.escape(profile.getSurname()) %>" autocomplete="family-name" required />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="email">Email</label>
						<input id="email" class="signin-form__input" type="email" name="email" maxlength="80"
							value="<%= HtmlUtils.escape(profile.getEmail()) %>" autocomplete="email" required />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="phone_number">Teléfono</label>
						<input id="phone_number" class="signin-form__input" type="tel" name="phone_number" maxlength="20"
							value="<%= HtmlUtils.escape(profile.getPhone_number()) %>" autocomplete="tel" />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="address">Dirección</label>
						<input id="address" class="signin-form__input" type="text" name="address" maxlength="80"
							value="<%= HtmlUtils.escape(profile.getAddress()) %>" autocomplete="street-address" />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="dni">DNI</label>
						<input id="dni" class="signin-form__input" type="text" name="dni" maxlength="10"
							value="<%= HtmlUtils.escape(profile.getDni()) %>" inputmode="numeric" autocomplete="off" />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="password">Nueva contraseña</label>
						<input id="password" class="signin-form__input" type="password" name="password" maxlength="200"
							placeholder="Dejala vacía para conservar la actual" autocomplete="new-password" />
					</div>

					<div class="signin-form__field">
						<label class="profile-form__label" for="confirm_password">Repetir nueva contraseña</label>
						<input id="confirm_password" class="signin-form__input" type="password" name="confirm_password" maxlength="200"
							placeholder="Repetí la nueva contraseña" autocomplete="new-password" />
					</div>

					<button class="signin-form__button" type="submit">Guardar cambios</button>
				</form>

				<p class="signin-form__register signup-back">
					<a href="MainHome" class="signin-form__link">Volver al menú</a>
				</p>
			</div>
		</section>
	</main>

	<script>
		document.addEventListener('DOMContentLoaded', () => {
			// reemplaza el globo nativo de "completá este campo" por un mensaje con el estilo de la app
			const form = document.getElementById('profileForm');
			form.addEventListener('submit', (e) => {
				const showError = (text) => {
					e.preventDefault();
					let errorEl = document.getElementById('profileClientError');
					if (!errorEl) {
						errorEl = document.createElement('p');
						errorEl.id = 'profileClientError';
						errorEl.className = 'profile-message profile-message--error';
						errorEl.setAttribute('role', 'alert');
						form.parentNode.insertBefore(errorEl, form);
					}
					errorEl.textContent = text;
				};

				if (!form.checkValidity()) {
					showError('Completá nombre, apellido y un email válido.');
					return;
				}
				if (form.password.value !== form.confirm_password.value) {
					showError('Las contraseñas no coinciden.');
				}
			});
		});
	</script>
</body>

</html>

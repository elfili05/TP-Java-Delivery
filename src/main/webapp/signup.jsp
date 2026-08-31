<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">

<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Registrarse</title>
	<link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" />
	<link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700;800&display=swap"
		rel="stylesheet" />
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
	<link rel="stylesheet" href="styles/main.css" />
	<link rel="icon" type="ico" href="assets/icon2.ico" />
	
	<%
	Boolean result = (Boolean) request.getAttribute("result");
	%>
</head>

<body class="page">
	<main class="signin-layout signup-layout">
	
		<section class="signin-panel signup-panel" aria-labelledby="signup-title">
			<div class="signin-brand" aria-label="Java Delivery">
				<div class="signin-brand__placeholder">
					<img src="assets/logo2.png" alt="Java Delivery" />
				</div>
			</div>

			<div class="signin-content">
				<h1 class="signin-title" id="signup-title">Registrarse</h1>

				<form class="signin-form signup-form" action="signup" method="post">
					<div class="signin-form__field">
						<label class="visually-hidden" for="name">Nombre</label>
						<input id="name" class="signin-form__input" type="text" name="name"
							placeholder="Ingrese su nombre..." autocomplete="given-name" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="surname">Apellido</label>
						<input id="surname" class="signin-form__input" type="text" name="surname"
							placeholder="Ingrese su apellido..." autocomplete="family-name" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="address">Dirección</label>
						<input id="address" class="signin-form__input" type="text" name="address"
							placeholder="Ingrese su dirección..." autocomplete="street-address" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="dni">DNI</label>
						<input id="dni" class="signin-form__input" type="text" name="dni"
							placeholder="Ingrese su DNI..." inputmode="numeric" autocomplete="off" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="phone_number">Teléfono</label>
						<input id="phone_number" class="signin-form__input" type="tel" name="phone_number"
							placeholder="Ingrese su teléfono..." autocomplete="tel" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="email">Email</label>
						<input id="email" class="signin-form__input" type="email" name="email"
							placeholder="Ingrese su email..." autocomplete="email" required />
					</div>

					<div class="signin-form__field">
						<label class="visually-hidden" for="password">Contraseña</label>
						<input id="password" class="signin-form__input" type="password" name="password"
							placeholder="Ingrese su contraseña..." autocomplete="new-password" required />
					</div>

					<button class="signin-form__button" type="submit">Registrarse</button>
				</form>


				<form action="signin" method="get">
					<p class="signin-form__register signup-back">
						¿Ya tienes una cuenta?
						<a href="signin" class="signin-form__link">
							<button class="signin-form__link" type="submit">Inicia sesión</button>
						</a>
					</p>
				</form>

			</div>
		</section>
		<% if (result != null) { %>
			<dialog open class="signup-modal">
				<div class="signup-modal__content">
					<h2 class="signup-modal__title">
						<%= result ? "¡Usuario agregado con éxito!" : "No se pudo agregar el usuario" %>
					</h2>
					<p class="signup-modal__text">
						<%= result
							? "Tu cuenta fue creada correctamente. Podés continuar con el inicio de sesión."
							: "Ocurrió un problema al guardar el usuario. Revisá los datos e intentá nuevamente." %>
					</p>
					<form class="signup-modal__form" action="<%= result ? "signin" : "signup" %>" method="get">
						<form method="dialog">
							<button class="signup-modal__button" value=<%=result ? null : "true" %> name=retry  type="submit">
								<%= result ? "Continuar" : "Reintentar" %>
							</button>
						</form>
					</form>
				</div>
			</dialog>
		<% } %>
	</main>
</body>

</html>
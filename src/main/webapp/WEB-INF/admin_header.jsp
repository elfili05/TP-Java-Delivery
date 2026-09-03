<%@ page pageEncoding="UTF-8"%>
<header class="admin-topbar">
	<a class="brand-block" href="AdminHome" aria-label="Java Delivery logo, ir al inicio">
		<img src="assets/icon2.ico" alt="Java Delivery logo"></img>
		<span class="brand-text">Java Delivery — Administrador</span>
	</a>

	<nav class="admin-nav" aria-label="Secciones de administración">
		<a href="AdminRestaurants" class="admin-nav__link">Gestionar Restaurantes</a>
		<a href="AdminUsers" class="admin-nav__link">Gestionar Usuarios</a>
	</nav>

	<div class="admin-topbar__right">
		<span class="admin-welcome">Hola, <strong><%= u.getName() %></strong></span>
		<form action="logout" method="post">
			<button type="submit" class="admin-logout">Cerrar sesión</button>
		</form>
	</div>
</header>

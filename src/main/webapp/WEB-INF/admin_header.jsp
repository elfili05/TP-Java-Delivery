<%@ page pageEncoding="UTF-8"%>
<%@ page import="main.java.entities.User"%>
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
		<a href="MainHome" class="admin-welcome" title="Volver al menú principal">Hola, <strong><%= u.getName() %></strong></a>
		<form action="logout" method="post">
			<button type="submit" class="admin-logout">Cerrar sesión</button>
		</form>
	</div>
</header>

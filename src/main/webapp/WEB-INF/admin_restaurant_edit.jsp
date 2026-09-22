<%@page import="java.util.LinkedList"%>
<%@page import="main.java.entities.Restaurant"%>
<%@page import="main.java.entities.Schedule"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
	// traduce el día de la semana (guardado en inglés en la DB) para mostrarlo en la lista de horarios.
	private static String dayLabel(String dayOfWeek) {
		switch (dayOfWeek.toLowerCase()) {
			case "monday": return "Lunes";
			case "tuesday": return "Martes";
			case "wednesday": return "Miércoles";
			case "thursday": return "Jueves";
			case "friday": return "Viernes";
			case "saturday": return "Sábado";
			case "sunday": return "Domingo";
			default: return dayOfWeek;
		}
	}
%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
	LinkedList<Schedule> schedules = (LinkedList<Schedule>) request.getAttribute("schedules");
	String message = (String) request.getAttribute("message");
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

		<main class="admin-content admin-content--wide">
			<section class="admin-panel" aria-label="Editar restaurante">
				<h1>Editar restaurante</h1>

				<% if (message != null) { %>
					<p class="admin-message"><%= message %></p>
				<% } %>

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

			<section class="admin-panel" aria-label="Horarios del restaurante">
				<div class="admin-section-header">
					<h1>Horarios</h1>
					<button type="button" id="openCreateSchedule" class="admin-submit admin-submit--small">Agregar horario</button>
				</div>

				<% if (schedules != null && !schedules.isEmpty()) { %>
					<ul class="admin-schedule-list">
						<% for (Schedule schedule : schedules) { %>
							<li class="admin-schedule-item">
								<div class="admin-schedule-item__info">
									<strong><%= dayLabel(schedule.getDay_of_week()) %></strong>
									<span> — <%= schedule.getStart_time().toString().substring(0, 5) %> a <%= schedule.getEnd_time().toString().substring(0, 5) %></span>
								</div>
								<div class="admin-schedule-item__actions">
									<a href="ScheduleEdit?restaurant_id=<%= restaurant.getRestaurant_id() %>&schedule_number=<%= schedule.getSchedule_number() %>" class="admin-action-link">Editar</a>
									<form action="ScheduleDelete" method="post" data-confirm="¿Eliminar este horario?">
										<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />
										<input type="hidden" name="schedule_number" value="<%= schedule.getSchedule_number() %>" />
										<button type="submit" class="admin-action-link admin-action-link--danger">Eliminar</button>
									</form>
								</div>
							</li>
						<% } %>
					</ul>
				<% } else { %>
					<p class="admin-empty">Este restaurante todavía no tiene horarios cargados.</p>
				<% } %>
			</section>

			<section class="admin-panel" aria-label="Productos del restaurante">
				<div class="admin-section-header">
					<h1>Productos</h1>
					<a href="AdminProducts?restaurant_id=<%= restaurant.getRestaurant_id() %>" class="admin-action-link">Gestionar productos</a>
				</div>
			</section>
		</main>

		<dialog id="createScheduleModal" class="admin-modal">
			<div class="admin-modal__content">
				<button type="button" id="closeCreateSchedule" class="admin-modal__close" aria-label="Cerrar">&times;</button>
				<h2>Agregar horario</h2>

				<form action="ScheduleCreate" method="post" class="admin-form">
					<input type="hidden" name="restaurant_id" value="<%= restaurant.getRestaurant_id() %>" />

					<label for="new_day_of_week">Día</label>
					<select id="new_day_of_week" name="day_of_week">
						<option value="monday">Lunes</option>
						<option value="tuesday">Martes</option>
						<option value="wednesday">Miércoles</option>
						<option value="thursday">Jueves</option>
						<option value="friday">Viernes</option>
						<option value="saturday">Sábado</option>
						<option value="sunday">Domingo</option>
					</select>

					<label for="new_start_time">Hora de apertura</label>
					<input type="time" id="new_start_time" name="start_time" required />

					<label for="new_end_time">Hora de cierre</label>
					<input type="time" id="new_end_time" name="end_time" required />

					<button type="submit" class="admin-submit">Agregar horario</button>
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
			// modal "Agregar horario"
			const scheduleModal = document.getElementById('createScheduleModal');
			const openScheduleBtn = document.getElementById('openCreateSchedule');
			const closeScheduleBtn = document.getElementById('closeCreateSchedule');
			if (openScheduleBtn) { openScheduleBtn.addEventListener('click', () => scheduleModal.showModal()); }
			if (closeScheduleBtn) { closeScheduleBtn.addEventListener('click', () => scheduleModal.close()); }

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

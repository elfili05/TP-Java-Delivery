<%@page import="main.java.entities.Schedule"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	Schedule editedSchedule = (Schedule) request.getAttribute("editedSchedule");
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Editar Horario</title>
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
			<section class="admin-panel" aria-label="Editar horario">
				<h1>Editar horario</h1>

				<form action="ScheduleEdit" method="post" class="admin-form">
					<input type="hidden" name="restaurant_id" value="<%= editedSchedule.getRestaurant_id() %>" />
					<input type="hidden" name="schedule_number" value="<%= editedSchedule.getSchedule_number() %>" />

					<label for="day_of_week">Día</label>
					<select id="day_of_week" name="day_of_week">
						<option value="monday" <%= "monday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Lunes</option>
						<option value="tuesday" <%= "tuesday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Martes</option>
						<option value="wednesday" <%= "wednesday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Miércoles</option>
						<option value="thursday" <%= "thursday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Jueves</option>
						<option value="friday" <%= "friday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Viernes</option>
						<option value="saturday" <%= "saturday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Sábado</option>
						<option value="sunday" <%= "sunday".equalsIgnoreCase(editedSchedule.getDay_of_week()) ? "selected" : "" %>>Domingo</option>
					</select>

					<label for="start_time">Hora de apertura</label>
					<input type="time" id="start_time" name="start_time" value="<%= editedSchedule.getStart_time().toString().substring(0, 5) %>" required />

					<label for="end_time">Hora de cierre</label>
					<input type="time" id="end_time" name="end_time" value="<%= editedSchedule.getEnd_time().toString().substring(0, 5) %>" required />

					<button type="submit" class="admin-submit">Guardar cambios</button>
				</form>

				<a href="RestaurantEdit?id=<%= editedSchedule.getRestaurant_id() %>" class="admin-cancel-link">Cancelar</a>
			</section>
		</main>
	</div>

	<script>
		document.addEventListener('DOMContentLoaded', () => {
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

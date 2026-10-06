<%@page import="java.util.LinkedList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="main.java.entities.Order"%>
<%@page import="main.java.entities.OrderDetail"%>
<%@page import="main.java.entities.User"%>
<%@page import="main.java.logic.HtmlUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	String message = (String) request.getAttribute("message");

	// variables que lee el fragmento admin_orders_list.jsp.
	LinkedList<Order> orders = (LinkedList<Order>) request.getAttribute("pendingOrders");
	boolean showDeliver = true;
	String ordersFrom = "home";
	String ordersEmptyText = "No hay pedidos pendientes.";
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Panel de Administrador</title>
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
			<section class="admin-panel" aria-label="Pedidos pendientes">
				<h1>Pedidos pendientes</h1>

				<% if (message != null) { %>
					<p class="admin-message"><%= HtmlUtils.escape(message) %></p>
				<% } %>

				<%@ include file="admin_orders_list.jsp" %>
			</section>
		</main>
	</div>
</body>
</html>

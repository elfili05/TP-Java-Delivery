<%@page import="java.util.LinkedList"%>
<%@page import="main.java.entities.Product"%>
<%@page import="main.java.entities.ProductType"%>
<%@page import="main.java.entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User u = (User) session.getAttribute("user");
	if (u == null || !"admin".equalsIgnoreCase(u.getRole())) {
		response.sendRedirect("index.html");
		return;
	}
	Integer restaurantId = (Integer) request.getAttribute("restaurantId");
	Product editedProduct = (Product) request.getAttribute("editedProduct");
	LinkedList<ProductType> productTypes = (LinkedList<ProductType>) request.getAttribute("productTypes");
	int currentTypeId = editedProduct.getProduct_type() != null ? editedProduct.getProduct_type().getProduct_type_id() : -1;
%>
<!DOCTYPE html>
<html lang="es">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Java Delivery | Editar Producto</title>
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
			<section class="admin-panel" aria-label="Editar producto">
				<h1>Editar producto</h1>

				<form action="ProductEdit" method="post" class="admin-form">
					<input type="hidden" name="restaurant_id" value="<%= restaurantId %>" />
					<input type="hidden" name="product_id" value="<%= editedProduct.getProduct_id() %>" />

					<label for="description">Descripción</label>
					<input type="text" id="description" name="description" value="<%= editedProduct.getDescription() %>" required />

					<label for="price">Precio</label>
					<input type="number" id="price" name="price" min="0.01" step="0.01" value="<%= editedProduct.getPrice() %>" required />

					<label for="product_type_id">Tipo de producto</label>
					<select id="product_type_id" name="product_type_id">
						<% for (ProductType productType : productTypes) { %>
							<option value="<%= productType.getProduct_type_id() %>" <%= productType.getProduct_type_id() == currentTypeId ? "selected" : "" %>><%= productType.getName() %></option>
						<% } %>
					</select>

					<button type="submit" class="admin-submit">Guardar cambios</button>
				</form>

				<a href="AdminProducts?restaurant_id=<%= restaurantId %>" class="admin-cancel-link">Cancelar</a>
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

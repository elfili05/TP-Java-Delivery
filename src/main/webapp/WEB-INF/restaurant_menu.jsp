<%@page import="main.java.entities.User"%>
<%@page import="main.java.entities.Restaurant"%>
<%@page import="main.java.entities.Product"%>
<%@page import="java.util.LinkedList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="icon" type="ico" href="assets/icon2.ico" />
<link rel="stylesheet" href="styles/main_page.css" />
<link rel="stylesheet" href="styles/restaurant_menu.css" />
<%
    Restaurant res = (Restaurant) session.getAttribute("currentRestaurant");
    User u = (User) session.getAttribute("user");
    String userName = (u != null && u.getName() != null && !u.getName().isBlank() && !"guest".equalsIgnoreCase(u.getRole())) ? u.getName() : "Invitado";
    String userAddress = (u != null && u.getAddress() != null && !u.getAddress().isBlank()) ? u.getAddress() : "Tu dirección";
    String restaurantName = res != null && res.getName() != null ? res.getName() : "Restaurante";
    String restaurantImage = res != null ? res.getImage_url() : null;
    LinkedList<Product> products = (LinkedList<Product>) request.getAttribute("products");
%>
<title>Java Delivery | <%= restaurantName %></title>
</head>
<body class="home-page menu-page">
    <div class="home-layout">
        <header class="topbar">
			<div class="brand-block" aria-label="Java Delivery logo">
				<img src="assets/icon2.ico" alt="Java Delivery logo"></img>
				<span class="brand-text">Java Delivery</span>
			</div>

			<div class="delivery-target">
				<span class="delivery-target__label">Enviar a:</span>
				<span class="delivery-target__value"><%= userAddress %></span>
			</div>

			<div class="user-menu-container">
				<button class="user-welcome" aria-label="Usuario logueado, menú de opciones" aria-haspopup="true" aria-expanded="false" id="userMenuBtn">
					<div class="user-welcome__avatar"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="size-6"><path fill-rule="evenodd" d="M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z" clip-rule="evenodd" /></svg>
					</div>
					<span class="user-welcome__text">Hola, <strong><%= userName %></strong></span>
					<%if (!u.getRole().equalsIgnoreCase("guest")) { %><svg class="user-welcome__arrow" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"> <% } %>
						<path fill-rule="evenodd" d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z" clip-rule="evenodd" />
					</svg>
				</button>
				
				<div class="user-dropdown" id="userDropdown" aria-label="Opciones de usuario">
					<ul class="user-dropdown__list">
						<li class="user-dropdown__item">
							<form action="logout" method="post">
								<button name="logoutButton" value="true" class="user-dropdown__link">
									<a href="logout" class="user-dropdown__link">
										<svg class="user-dropdown__icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor">
											<path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75" />
										</svg>
								<% if (!u.getRole().equalsIgnoreCase("guest")) { %> Cerrar sesión <% } else { %> Salir <% } %>
									</a>
								</button>
							</form>
						</li>
					</ul>
				</div>
				
			</div>
		</header>

        <section class="menu-banner" aria-label="Restaurante seleccionado"<% if (restaurantImage != null && !restaurantImage.isBlank()) { %> style="background-image: url('<%= restaurantImage %>')"<% } %>>
            <div class="menu-banner__overlay"><h1><%= restaurantName %></h1><p>Menú de productos</p></div>
        </section>

        <main class="menu-content">
            <div class="menu-toolbar">
            	<form action="restaurantmenu" method="get">
            		<label for="productTypeFilter">Filtrar por:</label>
            		<select id="productTypeFilter" name="productTypeFilter"><option value="all">Todos los productos
            				</option><% if (products != null) { java.util.LinkedHashSet<String> types = new java.util.LinkedHashSet<>(); for (Product product : products) { if (product.getProduct_type() != null && product.getProduct_type().getName() != null) types.add(product.getProduct_type().getName()); } for (String type : types) { %><option value="<%= type %>"><%= type %></option><% } } %>
            		</select>
            		<button type="submit">Elegir filtro</button>
            	</form>
            </div>
            <form id="orderForm" class="order-form">
            <div class="table-wrapper">
            	<table class="products-table">
            		<thead>
            			<tr>
            				<th scope="col">Descripción</th>
            				<th scope="col">Precio</th><th scope="col">Tipo de producto</th>
            				<th scope="col">Cantidad pedida</th>
            			</tr>
            		</thead>
            		 <tbody>
                			<% if (products != null) { for (Product product : products) { String type = product.getProduct_type() != null ? product.getProduct_type().getName() : "Sin tipo"; %>
                			<tr data-product-type="<%= type %>">
                				<td><%= product.getDescription() %></td>
                				<td>$ <%= String.format(java.util.Locale.US, "%.2f", product.getPrice()) %></td>
                				<td><%= type %></td>
                				<td>
                					<div class="quantity-control">
                							<button type="button" class="quantity-button decrease" aria-label="Reducir cantidad de <%= product.getDescription() %>">-</button>
                							<input type="number" name="quantity_<%= product.getProduct_id() %>" value="0" min="0" aria-label="Cantidad de <%= product.getDescription() %>">
                							<button type="button" class="quantity-button increase" aria-label="Aumentar cantidad de <%= product.getDescription() %>">+</button>
                					</div>
                				</td>
                			</tr>
                			<% } } %>
                	</tbody>
                </table>
            </div>
            <% if (products == null || products.isEmpty()) { %>
                <p class="menu-empty" role="status"><%= restaurantName %> no tiene productos para ofrecer.. por ahora.</p>
            <% } else { %>
                <div class="order-actions"><button type="button" class="order-button order-button--cancel" id="cancelOrder">Cancelar pedido</button><button type="submit" class="order-button order-button--confirm">Confirmar pedido</button></div>
            <% } %>
            </form>
        </main>
        <footer class="bottom-bar"><span>Java Delivery — 2026</span></footer>
    </div>

<script>
document.addEventListener('DOMContentLoaded', () => {
			const menuBtn = document.getElementById('userMenuBtn');
			const dropdown = document.getElementById('userDropdown');

			if (menuBtn && dropdown) {
				menuBtn.addEventListener('click', (e) => {
					e.stopPropagation();
					const isExpanded = menuBtn.getAttribute('aria-expanded') === 'true';
					menuBtn.setAttribute('aria-expanded', !isExpanded);
					dropdown.classList.toggle('is-active');
				});

				document.addEventListener('click', (e) => {
					if (!dropdown.contains(e.target) && !menuBtn.contains(e.target)) {
						menuBtn.setAttribute('aria-expanded', 'false');
						dropdown.classList.remove('is-active');
					}
				});

				// Escape key to close
				document.addEventListener('keydown', (e) => {
					if (e.key === 'Escape' && dropdown.classList.contains('is-active')) {
						menuBtn.setAttribute('aria-expanded', 'false');
						dropdown.classList.remove('is-active');
						menuBtn.focus();
					}
				});
			}
		});
</script>
</body>
</html>
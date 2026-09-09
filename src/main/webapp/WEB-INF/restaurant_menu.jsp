<%@page import="main.java.entities.Order"%>
<%@page import="main.java.entities.User"%>
<%@page import="main.java.entities.Restaurant"%>
<%@page import="main.java.entities.Product"%>
<%@page import="main.java.entities.OrderDetail"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.time.*"%>
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
<!-- <link rel="stylesheet" href="styles/main.css" /> -->
<%
	// load selected restaurant and user from session
    Restaurant res = (Restaurant) session.getAttribute("currentRestaurant");
    User u = (User) session.getAttribute("user");
 	// load selected restaurant and user from session
 
 	// prepare user and restaurant info for display
    String userName = (u != null && u.getName() != null && !u.getName().isBlank() && !"guest".equalsIgnoreCase(u.getRole())) ? u.getName() : "Invitado";
    String userAddress = (u != null && u.getAddress() != null && !u.getAddress().isBlank()) ? u.getAddress() : "Tu dirección";
    String restaurantName = res != null && res.getName() != null ? res.getName() : "Restaurante";
    String restaurantImage = res != null ? res.getImage_url() : null;
 	// prepare user and restaurant info for display
    
 	// load products for the selected restaurant from request attribute
    LinkedList<Product> products = (LinkedList<Product>) session.getAttribute("products");
 	// load products for the selected restaurant from request attribute
 	
 	Boolean confirmOrder = (Boolean) request.getAttribute("confirmOrder");
		Order userOrder = null;
		LinkedList<OrderDetail> orderDetails = new LinkedList<>();
		String orderDateLabel = "—";
		String orderAddressLabel = userAddress;
		String orderRestaurantLabel = restaurantName;
		double orderTotal = 0.0;
		double orderTotalWithDiscount = 0.0;
		boolean hasDiscount = false;
		String discountLabel = null;
	
		if (confirmOrder != null) {
			userOrder = (Order) session.getAttribute("order");
			if (userOrder != null) {
				if (userOrder.getOrder_details() != null) {
					orderDetails = userOrder.getOrder_details();
				}
				if (userOrder.getOrder_date() != null) {
					orderDateLabel = userOrder.getOrder_date().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				}
				if (userOrder.getUser() != null && userOrder.getUser().getAddress() != null && !userOrder.getUser().getAddress().isBlank()) {
					orderAddressLabel = userOrder.getUser().getAddress();
				}
				if (userOrder.getRestaurant() != null && userOrder.getRestaurant().getName() != null && !userOrder.getRestaurant().getName().isBlank()) {
					orderRestaurantLabel = userOrder.getRestaurant().getName();
				}
				for (OrderDetail detail : orderDetails) {
					if (detail != null) {
						orderTotal += detail.getSubtotal();
					}
				}
				if (userOrder.getDiscount() != null && userOrder.getDiscount().getDiscount_percentage() > 0) {
					hasDiscount = true;
					discountLabel = String.format(java.util.Locale.US, "%.0f%%", userOrder.getDiscount().getDiscount_percentage() * 100);
					orderTotalWithDiscount = userOrder.getTotalWithDiscount();
				}
			}
		}
 	
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
            				</option><% 
            				if (products != null) { 
            				java.util.LinkedHashSet<String> types = new java.util.LinkedHashSet<>(); 
            				for (Product product : products) { 
            				if (product.getProduct_type() != null && product.getProduct_type().getName() != null) {
            					types.add(product.getProduct_type().getName()); 
            					} 
            				}
            				
            				for (String type : types) { %>
            				<option value="<%= type %>"><%= type %></option>
            				<% } 
            				} %>
            				
            		</select>
            		<button type="submit">Elegir filtro</button>
            	</form>
            </div>
            <form id="orderForm" class="order-form" action="orderprocess" method="post">
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
			   <%  if (!u.getRole().equalsIgnoreCase("guest")) { %>
                <div class="order-actions">
                	<button type="submit" class="order-button order-button--confirm">Confirmar pedido</button>
                </div>
                <% } %>
            <% } %>
            </form>
            
			<form action="signin" method="post">
				<div class="order-actions">
                	<button type="submit" class="order-button order-button--cancel" id="cancelOrder">Cancelar pedido</button>
                </div>
            </form>
           
            <!-- Order confirmation modal -->
            <% if (confirmOrder != null) { %>
			<dialog open class="order-confirmation-dialog" aria-labelledby="orderConfirmationTitle">
				<div class="order-confirmation-dialog__panel">
					<header class="order-confirmation-dialog__header">
						<h2 class="order-confirmation-dialog__title" id="orderConfirmationTitle">Confirmación de pedido</h2>
					</header>

					<section class="order-confirmation-dialog__meta" aria-label="Datos del pedido">
						<div class="order-confirmation-dialog__meta-row">
							<span class="order-confirmation-dialog__label">Fecha pedido:</span>
							<span class="order-confirmation-dialog__value"><%= orderDateLabel %></span>
						</div>
						<div class="order-confirmation-dialog__meta-row">
							<span class="order-confirmation-dialog__label">Enviar a:</span>
							<span class="order-confirmation-dialog__value"><%= orderAddressLabel %></span>
						</div>
						<div class="order-confirmation-dialog__meta-row">
							<span class="order-confirmation-dialog__label">Restaurante:</span>
							<span class="order-confirmation-dialog__value"><%= orderRestaurantLabel %></span>
						</div>
					</section>

					<section class="order-confirmation-dialog__details" aria-label="Detalles del pedido">
						<div class="order-confirmation-dialog__section-title-wrap">
							<span class="order-confirmation-dialog__section-line" aria-hidden="true"></span>
							<h3 class="order-confirmation-dialog__section-title">Detalles del pedido</h3>
							<span class="order-confirmation-dialog__section-line" aria-hidden="true"></span>
						</div>

						<div class="order-confirmation-dialog__table-shell">
							<table class="order-confirmation-dialog__table">
								<thead>
									<tr>
										<th scope="col">Producto</th>
										<th scope="col">Precio</th>
										<th scope="col">Cantidad</th>
										<th scope="col">Subtotal</th>
									</tr>
								</thead>
								<tbody>
									<% if (orderDetails != null && !orderDetails.isEmpty()) { for (OrderDetail detail : orderDetails) { if (detail != null && detail.getProduct() != null) { %>
									<tr>
										<td><%= detail.getProduct().getDescription() %></td>
										<td>$ <%= String.format(java.util.Locale.US, "%.2f", detail.getProduct().getPrice()) %></td>
										<td><%= detail.getQuantity() %></td>
										<td>$ <%= String.format(java.util.Locale.US, "%.2f", detail.getSubtotal()) %></td>
									</tr>
									<% } } } else { %>
									<tr class="order-confirmation-dialog__empty-row">
										<td colspan="4">No hay productos para mostrar.</td>
									</tr>
									<% } %>
								</tbody>
							</table>
						</div>
					</section>

					<section class="order-confirmation-dialog__summary" aria-label="Resumen del pedido">
						<div class="order-confirmation-dialog__summary-row">
							<span class="order-confirmation-dialog__summary-label">Total:</span>
							<span class="order-confirmation-dialog__summary-value">$ <%= String.format(java.util.Locale.US, "%.2f", orderTotal) %></span>
						</div>
						<% if (hasDiscount) { %>
						<div class="order-confirmation-dialog__summary-row order-confirmation-dialog__summary-row--discount">
							<span class="order-confirmation-dialog__summary-label">Total con descuento (<%= discountLabel %> desc):</span>
							<span class="order-confirmation-dialog__summary-value">$ <%= String.format(java.util.Locale.US, "%.2f", orderTotalWithDiscount) %></span>
						</div>
						<% } %>
					</section>

					<div class="order-confirmation-dialog__actions">
						<form class="order-confirmation-dialog__action-form" action="orderProcess" method="get">
							<button class="order-confirmation-dialog__button order-confirmation-dialog__button--secondary" name="confirmOrder" value="false" type="submit">Cancelar pedido</button>
						</form>
						<form class="order-confirmation-dialog__action-form" action="orderProcess" method="get">
							<button class="order-confirmation-dialog__button order-confirmation-dialog__button--primary" name="confirmOrder" value="true" type="submit">Confirmar pedido</button>
						</form>
					</div>
				</div>
			</dialog>
		<% } %>
            
            
            
            
            
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
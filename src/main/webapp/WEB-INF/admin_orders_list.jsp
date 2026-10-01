<%@ page pageEncoding="UTF-8"%>
<%--
	Fragmento reutilizable que dibuja una lista de pedidos. La página que lo incluye con
	"<%@ include %>" debe importar LinkedList, Order, OrderDetail, HtmlUtils, BigDecimal,
	RoundingMode y DateTimeFormatter, y definir antes del include:
	LinkedList<Order> orders, boolean showDeliver, String ordersFrom, String ordersEmptyText.
--%>
<% if (orders == null || orders.isEmpty()) { %>
	<p class="admin-empty"><%= HtmlUtils.escape(ordersEmptyText) %></p>
<% } else { %>
	<ul class="admin-product-list">
		<% for (Order order : orders) {
			boolean isDelivered = "delivered".equals(order.getStatus());
		%>
			<li class="admin-product-item">
				<div class="admin-product-item__info">
					<strong>Pedido #<%= order.getOrder_id() %></strong>
					<span class="admin-role-badge <%= isDelivered ? "admin-role-badge--admin" : "" %>"><%= isDelivered ? "Entregado" : "Pendiente" %></span>
					<p>
						<%= order.getOrder_date() != null ? order.getOrder_date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-" %>
						&mdash; <%= HtmlUtils.escape(order.getUser().getName()) %> <%= HtmlUtils.escape(order.getUser().getSurname()) %>
						(<%= HtmlUtils.escape(order.getUser().getEmail()) %>)
						&mdash; <%= HtmlUtils.escape(order.getRestaurant().getName()) %>
					</p>
					<% for (OrderDetail detail : order.getOrder_details()) { %>
						<p><%= detail.getQuantity() %> &times; <%= HtmlUtils.escape(detail.getProduct().getDescription()) %> &mdash; $<%= String.format("%.2f", detail.getSubtotal()) %></p>
					<% } %>
					<% if (order.getDiscount() != null) { %>
						<p>Descuento aplicado: <%= BigDecimal.valueOf(order.getDiscount().getDiscount_percentage() * 100).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString().replace('.', ',') %>%</p>
					<% } %>
					<p><strong>Total: $<%= String.format("%.2f", order.getTotalWithDiscount()) %></strong></p>
				</div>
				<% if (showDeliver) { %>
					<div class="admin-product-item__actions">
						<form action="OrderDeliver" method="post">
							<input type="hidden" name="order_id" value="<%= order.getOrder_id() %>" />
							<input type="hidden" name="from" value="<%= HtmlUtils.escape(ordersFrom) %>" />
							<button type="submit" class="admin-action-link">Marcar como entregado</button>
						</form>
					</div>
				<% } %>
			</li>
		<% } %>
	</ul>
<% } %>

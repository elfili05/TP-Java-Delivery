package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.logic.ProcessOrder;
import main.java.logic.RestaurantCRUD;
import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.entities.Order;
import main.java.entities.OrderDetail;
import main.java.entities.Product;

import java.util.LinkedList;

/**
 * Servlet implementation class Order
 */
@WebServlet({ "/OrderProcess", "/orderProcess", "/ORDERPROCESS", "/Orderprocess","/orderprocess" })
public class OrderProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// tope de unidades por producto en un mismo pedido.
	private static final int MAX_QUANTITY = 99;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// un GET nunca guarda ni cancela un pedido: solo vuelve al menú. Confirmar/cancelar llega por POST (ver doPost).
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("user") == null || request.getSession().getAttribute("currentRestaurant") == null) {
			response.sendRedirect("index.html");
			return;
		}
		request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
	}

	// procesa la decisión de la ventana modal de confirmación: cancelar o confirmar el pedido que está en la sesión.
	private void handleConfirmation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// sesión vencida o sin iniciar: no hay a nombre de quién guardar nada.
		if (user == null) {
			response.sendRedirect("index.html");
			return;
		}

		if (!"true".equalsIgnoreCase(request.getParameter("confirmOrder"))) { // order cancellation
			session.removeAttribute("order");
			request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
			return;
		}

		// el pedido se saca de la sesión ANTES de guardarlo: si llegan dos confirmaciones a la vez (doble clic) solo una lo encuentra.
		Order orderToSave;
		synchronized (session) {
			orderToSave = (Order) session.getAttribute("order");
			session.removeAttribute("order");
			if (orderToSave != null) {
				// marca que este pedido se está guardando: una segunda confirmación (doble clic) ve la marca y muestra el "gracias" sin guardar de nuevo.
				session.setAttribute("orderSaved", Boolean.TRUE);
			}
		}

		// no hay pedido pendiente: o ya se guardó recién (doble clic o refresco: se muestra la confirmación) o no había nada que confirmar (se vuelve a la lista).
		if (orderToSave == null) {
			if (session.getAttribute("orderSaved") != null) {
				request.getRequestDispatcher("WEB-INF/order_confirmation.jsp").forward(request, response);
			} else {
				response.sendRedirect("MainHome");
			}
			return;
		}

		String notAllowedMessage = notAllowedToOrder(user);
		if (notAllowedMessage != null) {
			session.removeAttribute("orderSaved");
			showMenuWithError(request, response, notAllowedMessage);
			return;
		}

		// el pedido tiene que ser de quien está logueado (por ejemplo, si se cerró sesión y otra persona entró en el mismo navegador).
		if (orderToSave.getUser() == null || orderToSave.getUser().getUser_id() != user.getUser_id()) {
			session.removeAttribute("orderSaved");
			showMenuWithError(request, response, "El pedido no corresponde al usuario actual. Armalo de nuevo.");
			return;
		}

		try {
			// la disponibilidad se controla sobre el restaurante del PEDIDO, no sobre el último que se navegó.
			if (!new RestaurantCRUD().isAvailable(orderToSave.getRestaurant())) { // redirecting because restaurant is not available upon order confirmation
				session.removeAttribute("orderSaved");
				session.removeAttribute("currentRestaurant");
				session.removeAttribute("products");
				request.getRequestDispatcher("WEB-INF/restaurant_unavailable.jsp").forward(request, response); // order failed
				return;
			}

			if (new ProcessOrder().addOrder(orderToSave)) {
				request.getRequestDispatcher("WEB-INF/order_confirmation.jsp").forward(request, response); // order successfull
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// no se guardó nada (la transacción hizo rollback): el pedido vuelve a la sesión y se vuelve a mostrar el modal para poder reintentar.
		session.removeAttribute("orderSaved");
		session.setAttribute("order", orderToSave);
		request.setAttribute("confirmOrder", true);
		showMenuWithError(request, response, "No se pudo registrar el pedido. Intentá de nuevo.");
	}

	// solo los clientes con sesión iniciada piden: invitados y administradores no (el JSP oculta el botón, esto lo exige también el servidor).
	private String notAllowedToOrder(User user) {
		if ("guest".equalsIgnoreCase(user.getRole())) {
			return "Para hacer un pedido tenés que iniciar sesión con tu cuenta.";
		}
		if ("admin".equalsIgnoreCase(user.getRole())) {
			return "Los administradores no pueden hacer pedidos.";
		}
		return null;
	}

	private void showMenuWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
		request.setAttribute("orderError", errorMessage);
		request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// el modal de confirmación manda "confirmOrder" (true/false) por POST: se procesa aparte.
		if (request.getParameter("confirmOrder") != null) {
			handleConfirmation(request, response);
			return;
		}

		// If the user has selected products and quantities, prepare the order and redirect to confirmation modal. Otherwise, redirect back to menu.

		ProcessOrder proOrder = new ProcessOrder();

		LinkedList<Product> products = (LinkedList<Product>)request.getSession().getAttribute("products");
		Restaurant res = (Restaurant)request.getSession().getAttribute("currentRestaurant");
		User u = (User)request.getSession().getAttribute("user");

		// sesión vencida (o POST directo sin haber abierto un menú): se vuelve al inicio en vez de fallar.
		if (u == null || products == null || res == null) {
			response.sendRedirect("index.html");
			return;
		}

		String notAllowedMessage = notAllowedToOrder(u);
		if (notAllowedMessage != null) {
			showMenuWithError(request, response, notAllowedMessage);
			return;
		}

		// arranca un pedido nuevo: se olvida la marca de "pedido ya guardado" del anterior.
		request.getSession().removeAttribute("orderSaved");

		LinkedList<OrderDetail> orderDetails = new LinkedList<OrderDetail>();


		// create every order detail
		int detail_number = 1;
		for (Product product : products) {
			String quantityStr = request.getParameter("quantity_" + product.getProduct_id());
			if (quantityStr != null) {
				int quantity = parseQuantity(quantityStr);
				if (quantity > 0) {
					orderDetails.add(new OrderDetail(product, quantity, detail_number));
					detail_number++;
				}
			}
		}

		Order order = null;
		try {
			order = proOrder.prepareOrder(u, res, orderDetails);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getSession().setAttribute("order", order);

		if (order == null) { // no products were selected, empty order, redirecting back to menu

			doGet(request, response);
		}

		else { // order has items, proceed to confirmation modal.
			request.setAttribute("confirmOrder",true);
			request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
		}




		}

	// interpreta la cantidad del formulario: 0 (se ignora el ítem) si vino vacía o no es un número entero; el máximo es MAX_QUANTITY.
	private int parseQuantity(String rawQuantity) {
		try {
			return Math.min(Integer.parseInt(rawQuantity.trim()), MAX_QUANTITY);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}

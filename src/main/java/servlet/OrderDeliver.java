package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.OrderCRUD;

/**
 * Servlet implementation class OrderDeliver
 */
@WebServlet({ "/OrderDeliver", "/orderdeliver", "/orderDeliver", "/Orderdeliver", "/ORDERDELIVER" })
public class OrderDeliver extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderDeliver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// un GET nunca modifica datos: solo redirige al listado de pedidos.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		response.sendRedirect("AdminOrders");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// marca como entregado el pedido indicado por "order_id" y vuelve a la pantalla de origen ("from").
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer orderId = AdminProducts.parseId(request.getParameter("order_id"));

		String message;
		if (orderId == null || orderId <= 0) {
			message = "No se pudo marcar el pedido como entregado.";
		} else {
			OrderCRUD ctrlOrder = new OrderCRUD();

			try {
				Boolean delivered = ctrlOrder.deliverOrder(orderId);
				message = delivered ? "Pedido marcado como entregado." : "El pedido ya estaba entregado o no existe.";
			} catch (SQLException e) {
				e.printStackTrace();
				message = "No se pudo marcar el pedido como entregado. Intentá de nuevo.";
			}
		}

		request.setAttribute("message", message);

		// destinos permitidos tras el POST: "home" vuelve al inicio del panel; cualquier otro valor va al listado.
		if ("home".equals(request.getParameter("from"))) {
			AdminHome.forwardWithPendingOrders(request, response);
		} else {
			AdminOrders.forwardWithOrders(request, response);
		}
	}

}

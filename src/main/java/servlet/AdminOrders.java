package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Order;
import main.java.entities.User;
import main.java.logic.OrderCRUD;

/**
 * Servlet implementation class AdminOrders
 */
@WebServlet({ "/AdminOrders", "/adminorders", "/adminOrders", "/Adminorders", "/ADMINORDERS" })
public class AdminOrders extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminOrders() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los pedidos pendientes y entregados para la pantalla "Gestionar pedidos".
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		forwardWithOrders(request, response);
	}

	// recarga ambas listas de pedidos; la comparte OrderDeliver para volver a esta pantalla tras un POST.
	static void forwardWithOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OrderCRUD ctrlOrder = new OrderCRUD();

		LinkedList<Order> pendingOrders = new LinkedList<Order>();
		LinkedList<Order> deliveredOrders = new LinkedList<Order>();
		try {
			pendingOrders = ctrlOrder.getOrdersByStatus("pending");
			deliveredOrders = ctrlOrder.getOrdersByStatus("delivered");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("pendingOrders", pendingOrders);
		request.setAttribute("deliveredOrders", deliveredOrders);
		request.getRequestDispatcher("WEB-INF/admin_orders.jsp").forward(request, response);
	}

}

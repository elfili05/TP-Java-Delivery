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
 * Servlet implementation class AdminHome
 */
@WebServlet({ "/AdminHome", "/adminhome", "/adminHome", "/Adminhome", "/ADMINHOME" })
public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// punto de entrada al panel (inicio) para un admin ya logueado; es a donde vuelve el logo del header.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		forwardWithPendingOrders(request, response);
	}

	// un POST (a mano o por un formulario alterado) se trata igual que un GET: valida el rol y solo muestra la lista.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	// carga los pedidos pendientes y muestra el inicio del panel; la usa también OrderDeliver al volver a "home".
	static void forwardWithPendingOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OrderCRUD ctrlOrder = new OrderCRUD();

		LinkedList<Order> pendingOrders = new LinkedList<Order>();
		try {
			pendingOrders = ctrlOrder.getOrdersByStatus("pending");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("pendingOrders", pendingOrders);
		request.getRequestDispatcher("WEB-INF/admin_panel.jsp").forward(request, response);
	}

}

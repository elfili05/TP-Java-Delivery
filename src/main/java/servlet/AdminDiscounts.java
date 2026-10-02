package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Discount;
import main.java.entities.User;
import main.java.logic.DiscountCRUD;

/**
 * Servlet implementation class AdminDiscounts
 */
@WebServlet({ "/AdminDiscounts", "/admindiscounts", "/adminDiscounts", "/Admindiscounts", "/ADMINDISCOUNTS" })
public class AdminDiscounts extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminDiscounts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los descuentos para el panel admin (ordenados por monto mínimo, lo hace el repositorio).
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		forwardWithDiscounts(request, response);
	}

	// recarga el listado y vuelve a la pantalla de gestión de descuentos; la comparten los demás servlets de discount.
	static void forwardWithDiscounts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Flash.redirectAfterPost(request, response, "AdminDiscounts")) {
			return;
		}
		Flash.restore(request);

		LinkedList<Discount> discounts = new LinkedList<Discount>();
		try {
			discounts = new DiscountCRUD().getDiscounts();
		} catch (SQLException e) {
			e.printStackTrace();
			if (request.getAttribute("message") == null) {
				request.setAttribute("message", "No se pudieron cargar los descuentos. Intentá de nuevo.");
			}
		}

		request.setAttribute("discounts", discounts);
		request.getRequestDispatcher("WEB-INF/admin_discounts.jsp").forward(request, response);
	}

	// interpreta el id de la URL/formulario; null si vino vacío o no es un número.
	static Integer parseId(String rawId) {
		try {
			return Integer.parseInt(rawId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

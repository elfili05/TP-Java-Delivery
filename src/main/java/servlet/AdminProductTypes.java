package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.ProductType;
import main.java.entities.User;
import main.java.logic.ProductTypeCRUD;

/**
 * Servlet implementation class AdminProductTypes
 */
@WebServlet({ "/AdminProductTypes", "/adminproducttypes", "/adminProductTypes", "/Adminproducttypes", "/ADMINPRODUCTTYPES" })
public class AdminProductTypes extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminProductTypes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los tipos de producto para el panel admin.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		forwardWithProductTypes(request, response);
	}

	// recarga el listado y vuelve a la pantalla de gestión de tipos; la comparten los demás servlets de tipo de producto.
	static void forwardWithProductTypes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Flash.redirectAfterPost(request, response, "AdminProductTypes")) {
			return;
		}
		Flash.restore(request);

		LinkedList<ProductType> productTypes = new LinkedList<ProductType>();
		try {
			productTypes = new ProductTypeCRUD().getProductTypes();
		} catch (SQLException e) {
			e.printStackTrace();
			if (request.getAttribute("message") == null) {
				request.setAttribute("message", "No se pudieron cargar los tipos de producto. Intentá de nuevo.");
			}
		}

		request.setAttribute("productTypes", productTypes);
		request.getRequestDispatcher("WEB-INF/admin_product_types.jsp").forward(request, response);
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

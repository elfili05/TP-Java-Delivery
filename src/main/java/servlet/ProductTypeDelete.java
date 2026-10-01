package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.ProductTypeCRUD;

/**
 * Servlet implementation class ProductTypeDelete
 */
@WebServlet({ "/ProductTypeDelete", "/producttypedelete", "/productTypeDelete", "/Producttypedelete", "/PRODUCTTYPEDELETE" })
public class ProductTypeDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductTypeDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminProductTypes");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// borra el tipo indicado por el botón "Eliminar" del listado y vuelve al mismo listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer productTypeId = AdminProductTypes.parseId(request.getParameter("product_type_id"));

		String error;
		if (productTypeId == null) {
			error = "No se pudo eliminar el tipo de producto.";
		} else {
			try {
				error = new ProductTypeCRUD().deleteProductType(productTypeId);
			} catch (SQLException e) {
				e.printStackTrace();
				error = "No se pudo eliminar el tipo de producto.";
			}
		}

		request.setAttribute("message", error == null ? "Tipo de producto eliminado correctamente." : error);
		AdminProductTypes.forwardWithProductTypes(request, response);
	}

}

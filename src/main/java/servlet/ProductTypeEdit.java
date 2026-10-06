package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.ProductType;
import main.java.entities.User;
import main.java.logic.ProductTypeCRUD;

/**
 * Servlet implementation class ProductTypeEdit
 */
@WebServlet({ "/ProductTypeEdit", "/producttypeedit", "/productTypeEdit", "/Producttypeedit", "/PRODUCTTYPEEDIT" })
public class ProductTypeEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductTypeEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// trae el tipo elegido y precarga el formulario de edición.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer productTypeId = AdminProductTypes.parseId(request.getParameter("id"));
		if (productTypeId == null) {
			response.sendRedirect("AdminProductTypes");
			return;
		}

		ProductType productType = null;
		try {
			productType = new ProductTypeCRUD().getProductType(productTypeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (productType == null) {
			response.sendRedirect("AdminProductTypes");
			return;
		}

		request.setAttribute("productType", productType);
		request.getRequestDispatcher("WEB-INF/admin_product_type_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los cambios del formulario de edición y vuelve al listado con un mensaje.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer productTypeId = AdminProductTypes.parseId(request.getParameter("product_type_id"));

		String error;
		if (productTypeId == null) {
			error = "No se pudo actualizar el tipo de producto.";
		} else {
			try {
				error = new ProductTypeCRUD().updateProductType(productTypeId, request.getParameter("name"));
			} catch (SQLException e) {
				e.printStackTrace();
				error = "No se pudo actualizar el tipo de producto.";
			}
		}

		request.setAttribute("message", error == null ? "Tipo de producto actualizado correctamente." : error);
		AdminProductTypes.forwardWithProductTypes(request, response);
	}

}

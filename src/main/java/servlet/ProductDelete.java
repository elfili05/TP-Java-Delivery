package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.ProductCRUD;

/**
 * Servlet implementation class ProductDelete
 */
@WebServlet({ "/ProductDelete", "/productdelete", "/productDelete", "/Productdelete", "/PRODUCTDELETE" })
public class ProductDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// borra el producto indicado por el botón "Eliminar" del listado y vuelve al mismo listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = AdminProducts.parseId(request.getParameter("restaurant_id"));
		Integer productId = AdminProducts.parseId(request.getParameter("product_id"));

		ProductCRUD ctrlProduct = new ProductCRUD();

		Boolean deleted = false;
		if (productId != null) {
			try {
				ctrlProduct.deleteProduct(productId);
				deleted = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", deleted ? "Producto eliminado correctamente." : "No se pudo eliminar el producto.");
		AdminProducts.forwardWithProducts(request, response, restaurantId);
	}

}

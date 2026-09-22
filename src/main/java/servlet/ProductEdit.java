package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Product;
import main.java.entities.ProductType;
import main.java.entities.User;
import main.java.logic.ProductCRUD;

/**
 * Servlet implementation class ProductEdit
 */
@WebServlet({ "/ProductEdit", "/productedit", "/productEdit", "/Productedit", "/PRODUCTEDIT" })
public class ProductEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// trae el producto elegido y precarga el formulario de edición.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = AdminProducts.parseId(request.getParameter("restaurant_id"));
		Integer productId = AdminProducts.parseId(request.getParameter("product_id"));
		if (restaurantId == null || productId == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		ProductCRUD ctrlProduct = new ProductCRUD();
		Product product = null;
		LinkedList<ProductType> productTypes = new LinkedList<ProductType>();
		try {
			product = ctrlProduct.getProduct(productId);
			productTypes = ctrlProduct.getProductTypes();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (product == null) {
			response.sendRedirect("AdminProducts?restaurant_id=" + restaurantId);
			return;
		}

		request.setAttribute("restaurantId", restaurantId);
		request.setAttribute("editedProduct", product);
		request.setAttribute("productTypes", productTypes);
		request.getRequestDispatcher("WEB-INF/admin_product_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los cambios del producto y vuelve al listado de productos del restaurante.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = AdminProducts.parseId(request.getParameter("restaurant_id"));
		Integer productId = AdminProducts.parseId(request.getParameter("product_id"));
		Integer productTypeId = AdminProducts.parseId(request.getParameter("product_type_id"));
		String description = request.getParameter("description");
		Double price = ProductCreate.parsePrice(request.getParameter("price"));

		ProductCRUD ctrlProduct = new ProductCRUD();

		Boolean updated = false;
		if (productId != null && productTypeId != null && description != null && !description.trim().isEmpty() && price != null && price > 0) {
			Product product = new Product();
			product.setProduct_id(productId);
			product.setDescription(description);
			product.setPrice(price);

			ProductType productType = new ProductType();
			productType.setProduct_type_id(productTypeId);

			try {
				ctrlProduct.updateProduct(product, productType);
				updated = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", updated ? "Producto actualizado correctamente." : "No se pudo actualizar el producto.");
		AdminProducts.forwardWithProducts(request, response, restaurantId);
	}

}

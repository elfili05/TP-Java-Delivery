package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Product;
import main.java.entities.ProductType;
import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.logic.ProductCRUD;

/**
 * Servlet implementation class ProductCreate
 */
@WebServlet({ "/ProductCreate", "/productcreate", "/productCreate", "/Productcreate", "/PRODUCTCREATE" })
public class ProductCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminRestaurants");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// valida y crea un producto nuevo para el restaurante indicado por "restaurant_id".
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = AdminProducts.parseId(request.getParameter("restaurant_id"));
		Integer productTypeId = AdminProducts.parseId(request.getParameter("product_type_id"));
		String description = request.getParameter("description");
		Double price = parsePrice(request.getParameter("price"));

		ProductCRUD ctrlProduct = new ProductCRUD();

		Boolean created = false;
		if (restaurantId != null && productTypeId != null && description != null && !description.trim().isEmpty() && price != null && price > 0) {
			Restaurant restaurant = new Restaurant();
			restaurant.setRestaurant_id(restaurantId);

			ProductType productType = new ProductType();
			productType.setProduct_type_id(productTypeId);

			Product product = new Product();
			product.setDescription(description);
			product.setPrice(price);

			try {
				ctrlProduct.addProduct(product, restaurant, productType);
				created = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", created ? "Producto creado correctamente." : "No se pudo crear el producto. Revisá los datos ingresados.");
		AdminProducts.forwardWithProducts(request, response, restaurantId);
	}

	// interpreta el precio del formulario; null si vino vacío o no es un número válido.
	static Double parsePrice(String rawPrice) {
		try {
			return Double.parseDouble(rawPrice);
		} catch (NumberFormatException | NullPointerException e) {
			return null;
		}
	}

}

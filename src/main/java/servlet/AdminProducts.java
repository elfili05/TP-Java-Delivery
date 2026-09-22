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
import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.logic.ProductCRUD;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class AdminProducts
 */
@WebServlet({ "/AdminProducts", "/adminproducts", "/adminProducts", "/Adminproducts", "/ADMINPRODUCTS" })
public class AdminProducts extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminProducts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los productos del restaurante indicado por "restaurant_id" para el panel admin.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = parseId(request.getParameter("restaurant_id"));
		if (restaurantId == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		forwardWithProducts(request, response, restaurantId);
	}

	// recarga el restaurante, sus productos y los tipos disponibles; la comparten los demás servlets de product.
	static void forwardWithProducts(HttpServletRequest request, HttpServletResponse response, Integer restaurantId) throws ServletException, IOException {
		if (restaurantId == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		ProductCRUD ctrlProduct = new ProductCRUD();

		Restaurant restaurantToFind = new Restaurant();
		restaurantToFind.setRestaurant_id(restaurantId);

		Restaurant restaurant = null;
		LinkedList<Product> products = new LinkedList<Product>();
		LinkedList<ProductType> productTypes = new LinkedList<ProductType>();
		try {
			restaurant = ctrlRestaurant.getRestaurant(restaurantToFind);
			products = ctrlProduct.getProducts(restaurantToFind);
			productTypes = ctrlProduct.getProductTypes();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (restaurant == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		request.setAttribute("restaurant", restaurant);
		request.setAttribute("products", products);
		request.setAttribute("productTypes", productTypes);
		request.getRequestDispatcher("WEB-INF/admin_products.jsp").forward(request, response);
	}

	// interpreta el id de la URL/formulario; null si vino vacío o no es un número.
	static Integer parseId(String rawId) {
		try {
			return Integer.parseInt(rawId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class RestaurantCreate
 */
@WebServlet({ "/RestaurantCreate", "/restaurantcreate", "/restaurantCreate", "/Restaurantcreate", "/RESTAURANTCREATE" })
public class RestaurantCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantCreate() {
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
	// valida y crea un restaurante nuevo a partir del formulario del modal "Crear restaurante".
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		// solo un admin logueado puede crear restaurantes.
		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String imageUrl = request.getParameter("image_url");

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		if (name == null || name.trim().isEmpty() || address == null || address.trim().isEmpty()) {
			request.setAttribute("message", "El nombre y la dirección son obligatorios.");
			forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setName(name);
		restaurant.setAddress(address);
		restaurant.setImage_url(imageUrl);

		Boolean created = false;
		try {
			created = ctrlRestaurant.addRestaurant(restaurant);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("message", created ? "Restaurante creado correctamente." : "No se pudo crear el restaurante.");
		forwardWithRestaurants(request, response, ctrlRestaurant);
	}

	// recarga el listado y vuelve a la pantalla de gestión de restaurantes; la comparten los demás servlets de restaurant.
	static void forwardWithRestaurants(HttpServletRequest request, HttpServletResponse response, RestaurantCRUD ctrlRestaurant) throws ServletException, IOException {
		LinkedList<Restaurant> restaurants = new LinkedList<Restaurant>();
		try {
			restaurants = ctrlRestaurant.getAvailable();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("restaurants", restaurants);
		request.getRequestDispatcher("WEB-INF/admin_restaurants.jsp").forward(request, response);
	}

}

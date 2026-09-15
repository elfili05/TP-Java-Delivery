package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class RestaurantEdit
 */
@WebServlet({ "/RestaurantEdit", "/restaurantedit", "/restaurantEdit", "/Restaurantedit", "/RESTAURANTEDIT" })
public class RestaurantEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// trae el restaurante elegido y precarga el formulario de edición.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer restaurantId = parseId(request.getParameter("id"));
		if (restaurantId == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		Restaurant restaurantToFind = new Restaurant();
		restaurantToFind.setRestaurant_id(restaurantId);

		Restaurant restaurant = null;
		try {
			restaurant = ctrlRestaurant.getRestaurant(restaurantToFind);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (restaurant == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		request.setAttribute("restaurant", restaurant);
		request.getRequestDispatcher("WEB-INF/admin_restaurant_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los cambios del formulario de edición y vuelve al listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String imageUrl = request.getParameter("image_url");
		Integer restaurantId = parseId(request.getParameter("restaurant_id"));

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		Boolean updated = false;
		if (restaurantId != null && name != null && !name.trim().isEmpty() && address != null && !address.trim().isEmpty()) {
			Restaurant restaurant = new Restaurant();
			restaurant.setRestaurant_id(restaurantId);
			restaurant.setName(name);
			restaurant.setAddress(address);
			restaurant.setImage_url(imageUrl);
			try {
				updated = ctrlRestaurant.updateRestaurant(restaurant);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", updated ? "Restaurante actualizado correctamente." : "No se pudo actualizar el restaurante.");
		RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
	}

	// interpreta el id de la URL/formulario; null si vino vacío o no es un número.
	private Integer parseId(String rawId) {
		try {
			return Integer.parseInt(rawId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

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
 * Servlet implementation class AdminRestaurants
 */
@WebServlet({ "/AdminRestaurants", "/adminrestaurants", "/adminRestaurants", "/Adminrestaurants", "/ADMINRESTAURANTS" })
public class AdminRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminRestaurants() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los restaurantes para el panel admin, filtrando por nombre/dirección si vino "q".
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		LinkedList<Restaurant> restaurants = new LinkedList<Restaurant>();
		try {
			restaurants = ctrlRestaurant.getAvailable();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// filtro en memoria: para la cantidad de restaurantes esperada en este TP, alcanza sin armar un query aparte.
		String query = request.getParameter("q");
		if (query != null && !query.trim().isEmpty()) {
			String needle = query.trim().toLowerCase();
			LinkedList<Restaurant> filtered = new LinkedList<Restaurant>();
			for (Restaurant restaurant : restaurants) {
				boolean matchesName = restaurant.getName() != null && restaurant.getName().toLowerCase().contains(needle);
				boolean matchesAddress = restaurant.getAddress() != null && restaurant.getAddress().toLowerCase().contains(needle);
				if (matchesName || matchesAddress) {
					filtered.add(restaurant);
				}
			}
			restaurants = filtered;
		}

		request.setAttribute("restaurants", restaurants);
		request.setAttribute("query", query);
		request.getRequestDispatcher("WEB-INF/admin_restaurants.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

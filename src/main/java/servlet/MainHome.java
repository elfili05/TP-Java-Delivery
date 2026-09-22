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
 * Servlet implementation class MainHome
 */
@WebServlet({ "/MainHome", "/mainhome", "/mainHome", "/Mainhome", "/MAINHOME" })
public class MainHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// vuelve al menú principal (listado de restaurantes) para un usuario ya logueado; es a donde lleva el "Hola, usuario" del panel admin.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null) {
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

		request.setAttribute("restaurants", restaurants);
		request.getRequestDispatcher("WEB-INF/main_page.jsp").forward(request, response);
	}

}

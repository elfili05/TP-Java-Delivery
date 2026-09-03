package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class RestaurantDelete
 */
@WebServlet({ "/RestaurantDelete", "/restaurantdelete", "/restaurantDelete", "/Restaurantdelete", "/RESTAURANTDELETE" })
public class RestaurantDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// borra el restaurante indicado por el botón "Eliminar" del listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		Boolean deleted = false;
		try {
			int restaurantId = Integer.parseInt(request.getParameter("restaurant_id"));
			deleted = ctrlRestaurant.deleteRestaurant(restaurantId);
		} catch (NumberFormatException e) {
			// restaurant_id ausente o inválido: se trata igual que un borrado fallido.
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("message", deleted ? "Restaurante eliminado correctamente." : "No se pudo eliminar el restaurante.");
		RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
	}

}

package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;
import main.java.logic.RestaurantImageStorage;

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
		String failureMessage = "No se pudo eliminar el restaurante.";
		String imageUrl = null;
		try {
			int restaurantId = Integer.parseInt(request.getParameter("restaurant_id"));
			// se lee el restaurante antes de borrarlo para saber si tenía una imagen subida por la app.
			Restaurant restaurantToFind = new Restaurant();
			restaurantToFind.setRestaurant_id(restaurantId);
			Restaurant restaurant = ctrlRestaurant.getRestaurant(restaurantToFind);
			if (restaurant != null) {
				imageUrl = restaurant.getImage_url();
			}
			deleted = ctrlRestaurant.deleteRestaurant(restaurantId);
		} catch (NumberFormatException e) {
			// restaurant_id ausente o inválido: se trata igual que un borrado fallido.
		} catch (SQLIntegrityConstraintViolationException e) {
			failureMessage = "No se puede eliminar: el restaurante tiene productos o pedidos asociados.";
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (deleted) {
			// la BD ya confirmó el borrado: se limpia la imagen solo si la generó la app (nunca las semilla).
			RestaurantImageStorage.deleteIfGenerated(imageUrl, getServletContext());
		}

		request.setAttribute("message", deleted ? "Restaurante eliminado correctamente." : failureMessage);
		RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
	}

}

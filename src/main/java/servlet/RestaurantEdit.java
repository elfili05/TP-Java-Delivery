package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.java.entities.Restaurant;
import main.java.entities.Schedule;
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;
import main.java.logic.RestaurantImageStorage;

/**
 * Servlet implementation class RestaurantEdit
 */
@WebServlet({ "/RestaurantEdit", "/restaurantedit", "/restaurantEdit", "/Restaurantedit", "/RESTAURANTEDIT" })
// archivo de hasta 2 MB; el request completo tiene un margen extra para el resto del formulario.
@MultipartConfig(maxFileSize = RestaurantImageStorage.MAX_IMAGE_BYTES, maxRequestSize = RestaurantImageStorage.MAX_IMAGE_BYTES + 65536)
public class RestaurantEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// restaurant.name y restaurant.address son varchar(80).
	static final int MAX_TEXT_LENGTH = 80;

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

		forwardWithRestaurant(request, response, restaurantId);
	}

	// recarga el restaurante y sus horarios, y vuelve a la pantalla de edición; la comparten los servlets de horarios.
	static void forwardWithRestaurant(HttpServletRequest request, HttpServletResponse response, Integer restaurantId) throws ServletException, IOException {
		if (restaurantId == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		// después de un POST (horarios incluidos) se redirige a la pantalla (Post/Redirect/Get) para que F5 no repita la acción.
		if (Flash.redirectAfterPost(request, response, "RestaurantEdit?id=" + restaurantId)) {
			return;
		}
		Flash.restore(request);

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		Restaurant restaurantToFind = new Restaurant();
		restaurantToFind.setRestaurant_id(restaurantId);

		Restaurant restaurant = null;
		LinkedList<Schedule> schedules = new LinkedList<Schedule>();
		try {
			restaurant = ctrlRestaurant.getRestaurant(restaurantToFind);
			schedules = ctrlRestaurant.getSchedules(restaurantId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (restaurant == null) {
			response.sendRedirect("AdminRestaurants");
			return;
		}

		request.setAttribute("restaurant", restaurant);
		request.setAttribute("schedules", schedules);
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

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		String name = null;
		String address = null;
		Integer restaurantId = null;
		Part imagePart = null;
		try {
			name = request.getParameter("name");
			address = request.getParameter("address");
			restaurantId = parseId(request.getParameter("restaurant_id"));
			// si el request no vino como multipart (formulario alterado), no hay archivo que leer.
			if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
				imagePart = request.getPart("image");
			}
		} catch (IllegalStateException e) {
			// el contenedor aborta el parseo del request cuando un archivo supera el tope de multipart.
			request.setAttribute("message", "El archivo es demasiado grande (máximo 2 MB).");
			RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		if (name == null || name.trim().isEmpty() || address == null || address.trim().isEmpty()) {
			request.setAttribute("message", "El nombre y la dirección son obligatorios.");
			RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		if (name.trim().length() > MAX_TEXT_LENGTH || address.trim().length() > MAX_TEXT_LENGTH) {
			request.setAttribute("message", "El nombre y la dirección admiten hasta " + MAX_TEXT_LENGTH + " caracteres.");
			RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		if (restaurantId == null) {
			request.setAttribute("message", "No se pudo actualizar el restaurante.");
			RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		// se lee el restaurante antes de escribir nada en disco: si el id no existe no se guarda el archivo,
		// y si no viene imagen nueva se conserva la que ya tiene.
		Restaurant current = null;
		try {
			Restaurant restaurantToFind = new Restaurant();
			restaurantToFind.setRestaurant_id(restaurantId);
			current = ctrlRestaurant.getRestaurant(restaurantToFind);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (current == null) {
			request.setAttribute("message", "El restaurante indicado no existe.");
			RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		String newImageUrl = null;
		if (RestaurantImageStorage.hasImage(imagePart)) {
			try {
				newImageUrl = RestaurantImageStorage.store(imagePart, getServletContext());
			} catch (RestaurantImageStorage.ImageUploadException e) {
				request.setAttribute("message", e.getMessage());
				RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
				return;
			} catch (IOException e) {
				e.printStackTrace();
				request.setAttribute("message", "No se pudo guardar la imagen.");
				RestaurantCreate.forwardWithRestaurants(request, response, ctrlRestaurant);
				return;
			}
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setRestaurant_id(restaurantId);
		restaurant.setName(name.trim());
		restaurant.setAddress(address.trim());
		restaurant.setImage_url(newImageUrl != null ? newImageUrl : current.getImage_url());

		Boolean updated = false;
		try {
			updated = ctrlRestaurant.updateRestaurant(restaurant);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (newImageUrl != null) {
			if (updated) {
				// la BD ya quedó apuntando a la imagen nueva: recién ahora se borra la anterior,
				// solo si la generó la app (las semilla restaurantN.jpg nunca se tocan).
				RestaurantImageStorage.deleteIfGenerated(current.getImage_url(), getServletContext());
			} else {
				// el UPDATE falló: el archivo recién escrito no puede quedar huérfano en uploads/.
				RestaurantImageStorage.deleteIfGenerated(newImageUrl, getServletContext());
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

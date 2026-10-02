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
import main.java.entities.User;
import main.java.logic.RestaurantCRUD;
import main.java.logic.RestaurantImageStorage;

/**
 * Servlet implementation class RestaurantCreate
 */
@WebServlet({ "/RestaurantCreate", "/restaurantcreate", "/restaurantCreate", "/Restaurantcreate", "/RESTAURANTCREATE" })
// archivo de hasta 2 MB; el request completo tiene un margen extra para el resto del formulario.
@MultipartConfig(maxFileSize = RestaurantImageStorage.MAX_IMAGE_BYTES, maxRequestSize = RestaurantImageStorage.MAX_IMAGE_BYTES + 65536)
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
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

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

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();

		String name = null;
		String address = null;
		Part imagePart = null;
		try {
			name = request.getParameter("name");
			address = request.getParameter("address");
			// si el request no vino como multipart (formulario alterado), no hay archivo que leer.
			if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
				imagePart = request.getPart("image");
			}
		} catch (IllegalStateException e) {
			// el contenedor aborta el parseo del request cuando un archivo supera el tope de multipart.
			request.setAttribute("message", "El archivo es demasiado grande (máximo 2 MB).");
			request.setAttribute("reopenCreateModal", true);
			forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		if (name == null || name.trim().isEmpty() || address == null || address.trim().isEmpty()) {
			request.setAttribute("message", "El nombre y la dirección son obligatorios.");
			request.setAttribute("reopenCreateModal", true);
			forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		if (name.trim().length() > RestaurantEdit.MAX_TEXT_LENGTH || address.trim().length() > RestaurantEdit.MAX_TEXT_LENGTH) {
			request.setAttribute("message", "El nombre y la dirección admiten hasta " + RestaurantEdit.MAX_TEXT_LENGTH + " caracteres.");
			request.setAttribute("reopenCreateModal", true);
			forwardWithRestaurants(request, response, ctrlRestaurant);
			return;
		}

		// primero se valida y se guarda la imagen (opcional); recién después se toca la BD.
		String imageUrl = null;
		if (RestaurantImageStorage.hasImage(imagePart)) {
			try {
				imageUrl = RestaurantImageStorage.store(imagePart, getServletContext());
			} catch (RestaurantImageStorage.ImageUploadException e) {
				request.setAttribute("message", e.getMessage());
				request.setAttribute("reopenCreateModal", true);
				forwardWithRestaurants(request, response, ctrlRestaurant);
				return;
			} catch (IOException e) {
				e.printStackTrace();
				request.setAttribute("message", "No se pudo guardar la imagen.");
				request.setAttribute("reopenCreateModal", true);
				forwardWithRestaurants(request, response, ctrlRestaurant);
				return;
			}
		}

		Restaurant restaurant = new Restaurant();
		restaurant.setName(name.trim());
		restaurant.setAddress(address.trim());
		restaurant.setImage_url(imageUrl);

		Boolean created = false;
		try {
			created = ctrlRestaurant.addRestaurant(restaurant);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!created && imageUrl != null) {
			// el INSERT falló: el archivo recién escrito no puede quedar huérfano en uploads/.
			RestaurantImageStorage.deleteIfGenerated(imageUrl, getServletContext());
		}

		request.setAttribute("message", created ? "Restaurante creado correctamente." : "No se pudo crear el restaurante.");
		if (!created) {
			request.setAttribute("reopenCreateModal", true);
		}
		forwardWithRestaurants(request, response, ctrlRestaurant);
	}

	// recarga el listado y vuelve a la pantalla de gestión de restaurantes; la comparten los demás servlets de restaurant.
	static void forwardWithRestaurants(HttpServletRequest request, HttpServletResponse response, RestaurantCRUD ctrlRestaurant) throws ServletException, IOException {
		// después de un POST se redirige al listado (Post/Redirect/Get) para que F5 no vuelva a crear lo mismo.
		if (Flash.redirectAfterPost(request, response, "AdminRestaurants")) {
			return;
		}

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

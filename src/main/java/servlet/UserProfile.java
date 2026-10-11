package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.UserCRUD;

/**
 * Servlet implementation class UserProfile
 */
@WebServlet({ "/UserProfile", "/userprofile", "/userProfile", "/Userprofile", "/USERPROFILE" })
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	// solo un usuario registrado (cliente o admin) puede tener perfil: el invitado no existe en la base de datos.
	private static boolean canEditProfile(User u) {
		return u != null && u.getRole() != null && !u.getRole().equalsIgnoreCase("guest") && u.getUser_id() > 0;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// muestra el formulario con los datos actuales del usuario logueado (se releen de la base, no de la sesión).
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (!canEditProfile(u)) {
			response.sendRedirect("index.html");
			return;
		}

		Flash.restore(request);

		User profile = null;
		try {
			profile = new UserCRUD().getById(u.getUser_id());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("profileUser", profile != null ? profile : u);
		request.getRequestDispatcher("WEB-INF/user_profile.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los datos propios del usuario; el id y el rol salen siempre de la sesión, nunca del request.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (!canEditProfile(u)) {
			response.sendRedirect("index.html");
			return;
		}

		User edited = new User();
		edited.setUser_id(u.getUser_id());
		edited.setRole(u.getRole());
		edited.setName(trimmed(request.getParameter("name")));
		edited.setSurname(trimmed(request.getParameter("surname")));
		edited.setEmail(trimmed(request.getParameter("email")));
		edited.setPhone_number(trimmed(request.getParameter("phone_number")));
		edited.setDni(trimmed(request.getParameter("dni")));
		edited.setAddress(trimmed(request.getParameter("address")));

		String newPassword = request.getParameter("password");
		String confirmPassword = request.getParameter("confirm_password");
		edited.setPassword(newPassword);

		// límites de las columnas de la tabla user (name, surname, address, email: 80; dni: 10; phone_number: 20; password: 200).
		if (tooLong(edited.getName(), 80) || tooLong(edited.getSurname(), 80) || tooLong(edited.getAddress(), 80)
				|| tooLong(edited.getEmail(), 80) || tooLong(edited.getDni(), 10) || tooLong(edited.getPhone_number(), 20)
				|| tooLong(newPassword, 200)) {
			forwardWithError(request, response, edited, "Algún dato supera el largo permitido (nombre, apellido, dirección y email: 80; DNI: 10; teléfono: 20; contraseña: 200).");
			return;
		}
		// el "required" del formulario solo existe en el navegador: el servidor exige lo mismo (y un email con formato válido).
		if (!notBlank(edited.getName()) || !notBlank(edited.getSurname()) || edited.getEmail() == null
				|| !edited.getEmail().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
			forwardWithError(request, response, edited, "Completá nombre, apellido y un email válido.");
			return;
		}
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
			forwardWithError(request, response, edited, "Las contraseñas no coinciden.");
			return;
		}

		boolean updated = false;
		try {
			updated = new UserCRUD().updateProfile(edited);
		} catch (SQLIntegrityConstraintViolationException e) {
			// la columna email es UNIQUE: otro usuario ya usa ese email.
			forwardWithError(request, response, edited, "Ese email ya está en uso por otro usuario.");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!updated) {
			forwardWithError(request, response, edited, "No se pudieron actualizar tus datos.");
			return;
		}

		// la sesión sigue siendo la fuente de "Hola, nombre" y de la dirección de envío: se actualiza con los datos nuevos (sin la password).
		edited.setPassword(null);
		request.getSession().setAttribute("user", edited);

		request.setAttribute("message", "Tus datos se actualizaron correctamente.");
		if (Flash.redirectAfterPost(request, response, "UserProfile")) {
			return;
		}
		doGet(request, response);
	}

	// vuelve al formulario conservando lo que el usuario escribió (sin la password) y mostrando el motivo del rechazo.
	private void forwardWithError(HttpServletRequest request, HttpServletResponse response, User entered, String message) throws ServletException, IOException {
		entered.setPassword(null);
		request.setAttribute("profileUser", entered);
		request.setAttribute("message", message);
		request.setAttribute("isError", true);
		request.getRequestDispatcher("WEB-INF/user_profile.jsp").forward(request, response);
	}

	private static String trimmed(String value) {
		return value == null ? null : value.trim();
	}

	private static boolean notBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

	private static boolean tooLong(String value, int maxLength) {
		return value != null && value.length() > maxLength;
	}

}

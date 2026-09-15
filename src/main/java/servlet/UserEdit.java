package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.UserCRUD;

/**
 * Servlet implementation class UserEdit
 */
@WebServlet({ "/UserEdit", "/useredit", "/userEdit", "/Useredit", "/USEREDIT" })
public class UserEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// busca al usuario por email (no hay una consulta puntual, se recorre getAll()) y precarga el formulario.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String email = request.getParameter("email");
		User userToEdit = null;
		try {
			LinkedList<User> users = new UserCRUD().getAll();
			for (User candidate : users) {
				if (candidate.getEmail().equalsIgnoreCase(email)) {
					userToEdit = candidate;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (userToEdit == null) {
			response.sendRedirect("AdminUsers");
			return;
		}

		request.setAttribute("editedUser", userToEdit);
		request.getRequestDispatcher("WEB-INF/admin_user_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda nombre/apellido/contacto y rol del usuario editado (nunca la contraseña) y vuelve al listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String role = request.getParameter("role");

		UserCRUD ctrlUser = new UserCRUD();

		Boolean updated = false;
		if (name != null && !name.trim().isEmpty() && surname != null && !surname.trim().isEmpty()) {
			User userToUpdate = new User();
			userToUpdate.setEmail(email);
			userToUpdate.setName(name);
			userToUpdate.setSurname(surname);
			userToUpdate.setPhone_number(request.getParameter("phone_number"));
			userToUpdate.setDni(request.getParameter("dni"));
			userToUpdate.setAddress(request.getParameter("address"));
			userToUpdate.setRole(role);

			try {
				updated = ctrlUser.updateUser(userToUpdate);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("message", updated ? "Usuario actualizado correctamente." : "No se pudo actualizar el usuario.");
		forwardWithUsers(request, response, ctrlUser);
	}

	// recarga el listado y vuelve a la pantalla de gestión de usuarios; la comparten los demás servlets de user.
	static void forwardWithUsers(HttpServletRequest request, HttpServletResponse response, UserCRUD ctrlUser) throws ServletException, IOException {
		LinkedList<User> users = new LinkedList<User>();
		try {
			users = ctrlUser.getAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.setAttribute("users", users);
		request.getRequestDispatcher("WEB-INF/admin_users.jsp").forward(request, response);
	}

}

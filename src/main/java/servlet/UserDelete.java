package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.UserCRUD;

/**
 * Servlet implementation class UserDelete
 */
@WebServlet({ "/UserDelete", "/userdelete", "/userDelete", "/Userdelete", "/USERDELETE" })
public class UserDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// borra el usuario indicado por el botón "Eliminar" del listado, salvo que sea el admin logueado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String email = request.getParameter("email");
		UserCRUD ctrlUser = new UserCRUD();

		Boolean deleted = false;
		String message;
		if (email == null || email.trim().isEmpty()) {
			message = "No se pudo eliminar el usuario.";
		} else if (email.equalsIgnoreCase(u.getEmail())) {
			// un admin no puede eliminarse a si mismo mientras esta logueado.
			message = "No podés eliminar tu propio usuario mientras estás logueado.";
		} else {
			try {
				deleted = ctrlUser.deleteUser(email);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			message = deleted ? "Usuario eliminado correctamente." : "No se pudo eliminar el usuario.";
		}

		request.setAttribute("message", message);
		UserEdit.forwardWithUsers(request, response, ctrlUser);
	}

}

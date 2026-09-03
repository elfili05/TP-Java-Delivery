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
 * Servlet implementation class AdminUsers
 */
@WebServlet({ "/AdminUsers", "/adminusers", "/adminUsers", "/Adminusers", "/ADMINUSERS" })
public class AdminUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// lista los usuarios para el panel admin, filtrando por nombre/apellido/email si vino "q".
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		UserCRUD ctrlUser = new UserCRUD();
		LinkedList<User> users = new LinkedList<User>();
		try {
			users = ctrlUser.getAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query = request.getParameter("q");
		if (query != null && !query.trim().isEmpty()) {
			String needle = query.trim().toLowerCase();
			LinkedList<User> filtered = new LinkedList<User>();
			for (User user : users) {
				boolean matchesName = user.getName() != null && user.getName().toLowerCase().contains(needle);
				boolean matchesSurname = user.getSurname() != null && user.getSurname().toLowerCase().contains(needle);
				boolean matchesEmail = user.getEmail() != null && user.getEmail().toLowerCase().contains(needle);
				if (matchesName || matchesSurname || matchesEmail) {
					filtered.add(user);
				}
			}
			users = filtered;
		}

		request.setAttribute("users", users);
		request.setAttribute("query", query);
		request.getRequestDispatcher("WEB-INF/admin_users.jsp").forward(request, response);
	}

}

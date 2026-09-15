package main.java.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;

/**
 * Servlet implementation class AdminHome
 */
@WebServlet({ "/AdminHome", "/adminhome", "/adminHome", "/Adminhome", "/ADMINHOME" })
public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// punto de entrada al panel (inicio) para un admin ya logueado; es a donde vuelve el logo del header.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		request.getRequestDispatcher("WEB-INF/admin_panel.jsp").forward(request, response);
	}

}

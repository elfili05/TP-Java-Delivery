package main.java.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
@WebServlet({ "/Logout", "/logout", "/LogOut", "/logOut", "/LOGOUT" })
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// un GET no cierra la sesión (una página externa podría forzarlo con un link o una imagen): solo vuelve al inicio. El cierre es por POST.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// se descarta toda la sesión (usuario, pedido en curso, menú): así nadie que entre después en el mismo navegador hereda nada.
		request.getSession().invalidate();
		request.getRequestDispatcher("index.html").forward(request, response);
	}

}

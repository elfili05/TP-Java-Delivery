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
 * Servlet implementation class Signin
 */
@WebServlet({ "/Signin", "/SignIn", "/signin", "/signIn", "/SIGNIN" })
public class Signin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("index.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserCRUD ctrlUser = new UserCRUD();
		User u = new User();
		
		u.setRole(request.getParameter("role")); // solo en el caso de "guest" se usará esta parte.
		u.setEmail(request.getParameter("email"));
		u.setPassword(request.getParameter("password"));
		
		
		
		// atrapamos toda excepcion que pueda suceder en nuestro acceso a la DB.
		try {
			u = ctrlUser.validateUser(u);
		} catch (SQLException e) {
			response.getWriter().append(e.toString());
			System.out.println("exception");
			//e.printStackTrace();
		}
		
		if (u.getRole() != null) {
			
			if (u.getRole().equalsIgnoreCase("client") || u.getRole().equalsIgnoreCase("guest")){
			request.getSession().setAttribute("user", u);
			request.getRequestDispatcher("main_page.jsp").forward(request, response);
				}

			}
		else {
			request.getRequestDispatcher("WEB-INF/signin_error.html").include(request, response);
//			response.getWriter().append("Email o Contrasena incorrectos.");
		}
	}
	/* 2 formas de continuar flujo:
	 * - forward: envía a traves del propio servlet, la trae y la devuelve en la url del servlet. en la misma peticion, yo respondo otra pagina y todo
	 * el circuito va por dentro del servidor. solo se puede llegar mediante el servlet.
	 * - redirect: se le envia la resp al cliente y se lo redirige a otra página.
	 * */
}

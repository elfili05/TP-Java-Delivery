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
import main.java.entities.Restaurant;
import main.java.logic.RestaurantCRUD;
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
		

		
		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		UserCRUD ctrlUser = new UserCRUD();
		User u = new User();
		LinkedList<Restaurant> restaurants = new LinkedList<Restaurant>();
		
		
		
		// el rol NUNCA se toma del request: sale de la base de datos. Solo "guest" (botón de invitado) se acepta como valor pedido.
		String requestedRole = request.getParameter("role");
		u.setEmail(request.getParameter("email"));
		u.setPassword(request.getParameter("password"));
		
		
		
		
		// atrapamos toda excepcion que pueda suceder en nuestro acceso a la DB.
		try {
			u = ctrlUser.validateUser(u);
		} catch (SQLException e) {
			// el detalle queda en el servidor; al usuario no se le muestra la excepción.
			e.printStackTrace();
		}
		
		// validateUser devuelve el mismo objeto sin rol si las credenciales no existen: solo ahí se permite entrar como invitado.
		if (u.getRole() == null && "guest".equalsIgnoreCase(requestedRole)) {
			u.setRole("guest");
		}

		// si no vino ni email ni el botón de invitado (por ejemplo el botón "volver al menú"), se sigue con el usuario que ya está en la sesión.
		boolean loginAttempt = request.getParameter("email") != null || "guest".equalsIgnoreCase(requestedRole);
		if (!loginAttempt && request.getSession().getAttribute("user") != null) {
			u = (User) request.getSession().getAttribute("user");
			// volver a la lista de restaurantes abandona el pedido que se estaba armando ("Cancelar pedido").
			request.getSession().removeAttribute("order");
			
		}
		

		
		if (u.getRole() != null) {

				try {
					restaurants = ctrlRestaurant.getAvailable();
				} catch (SQLException e) {
					e.printStackTrace();

				}
				
				// un login nuevo siempre arranca una sesión nueva: no hereda el pedido ni el menú de quien estaba antes
				// y el id de sesión cambia al autenticarse (evita la fijación de sesión).
				if (loginAttempt) {
					request.getSession().invalidate();
					request.getSession(true).setAttribute("user", u);
				}
				
				request.setAttribute("restaurants", restaurants);
				request.getRequestDispatcher("WEB-INF/main_page.jsp").forward(request, response);

				
				

				}
			else {
			request.getRequestDispatcher("WEB-INF/signin_error.html").include(request, response);
			//response.getWriter().append("Email o Contrasena incorrectos.");
				}


			}

		
	}
	/* 2 formas de continuar flujo:
	 * - forward: envía a traves del propio servlet, la trae y la devuelve en la url del servlet. en la misma peticion, yo respondo otra pagina y todo
	 * el circuito va por dentro del servidor. solo se puede llegar mediante el servlet.
	 * - redirect: se le envia la resp al cliente y se lo redirige a otra página.
	 * */


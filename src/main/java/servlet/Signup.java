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
 * Servlet implementation class Signup
 */
@WebServlet({ "/Signup", "/signup", "/SignUp", "/signUp", "/SIGNUP"})
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getAttribute("retry") != null) {
			request.removeAttribute("result");
			request.removeAttribute("retry");
			}
		request.getRequestDispatcher("signup.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User newUser = new User();
		UserCRUD ctrlUser = new UserCRUD();
		
		newUser.setAddress(request.getParameter("address"));
		newUser.setEmail(request.getParameter("email"));
		newUser.setName(request.getParameter("name"));
		newUser.setSurname(request.getParameter("surname"));
		newUser.setPassword(request.getParameter("password"));
		newUser.setPhone_number(request.getParameter("phone_number"));
		newUser.setDni(request.getParameter("dni"));
		newUser.setRole("client"); // solo se cargan clientes desde la UI.
		
		try {
			Boolean result = ctrlUser.addUser(newUser);
			request.setAttribute("result",result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	if (request.getAttribute("result") != null) {
			request.getRequestDispatcher("signup.jsp").forward(request, response);
		}
	}

}

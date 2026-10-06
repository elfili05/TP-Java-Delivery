package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.DiscountCRUD;

/**
 * Servlet implementation class DiscountCreate
 */
@WebServlet({ "/DiscountCreate", "/discountcreate", "/discountCreate", "/Discountcreate", "/DISCOUNTCREATE" })
public class DiscountCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiscountCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminDiscounts");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// valida y crea un descuento nuevo desde el modal "Crear descuento" del listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String error;
		try {
			error = new DiscountCRUD().addDiscount(
					request.getParameter("minimum_amount"),
					request.getParameter("discount_percentage")
					);
		} catch (SQLException e) {
			e.printStackTrace();
			error = "No se pudo crear el descuento.";
		}

		request.setAttribute("message", error == null ? "Descuento creado correctamente." : error);
		AdminDiscounts.forwardWithDiscounts(request, response);
	}

}

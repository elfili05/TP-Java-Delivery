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
 * Servlet implementation class DiscountDelete
 */
@WebServlet({ "/DiscountDelete", "/discountdelete", "/discountDelete", "/Discountdelete", "/DISCOUNTDELETE" })
public class DiscountDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiscountDelete() {
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
	// borra el descuento indicado por el botón "Eliminar" del listado y vuelve al mismo listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer discountId = AdminDiscounts.parseId(request.getParameter("discount_id"));

		String error;
		if (discountId == null) {
			error = "No se pudo eliminar el descuento.";
		} else {
			try {
				error = new DiscountCRUD().deleteDiscount(discountId);
			} catch (SQLException e) {
				e.printStackTrace();
				error = "No se pudo eliminar el descuento.";
			}
		}

		request.setAttribute("message", error == null ? "Descuento eliminado correctamente." : error);
		AdminDiscounts.forwardWithDiscounts(request, response);
	}

}

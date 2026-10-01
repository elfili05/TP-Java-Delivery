package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.Discount;
import main.java.entities.User;
import main.java.logic.DiscountCRUD;

/**
 * Servlet implementation class DiscountEdit
 */
@WebServlet({ "/DiscountEdit", "/discountedit", "/discountEdit", "/Discountedit", "/DISCOUNTEDIT" })
public class DiscountEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DiscountEdit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// trae el descuento elegido y precarga el formulario de edición.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer discountId = AdminDiscounts.parseId(request.getParameter("id"));
		if (discountId == null) {
			response.sendRedirect("AdminDiscounts");
			return;
		}

		Discount discount = null;
		try {
			discount = new DiscountCRUD().getDiscount(discountId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (discount == null) {
			response.sendRedirect("AdminDiscounts");
			return;
		}

		request.setAttribute("discount", discount);
		request.getRequestDispatcher("WEB-INF/admin_discount_edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// guarda los cambios del formulario de edición y vuelve al listado con un mensaje.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		Integer discountId = AdminDiscounts.parseId(request.getParameter("discount_id"));

		String error;
		if (discountId == null) {
			error = "No se pudo actualizar el descuento.";
		} else {
			try {
				error = new DiscountCRUD().updateDiscount(
						discountId,
						request.getParameter("minimum_amount"),
						request.getParameter("discount_percentage")
						);
			} catch (SQLException e) {
				e.printStackTrace();
				error = "No se pudo actualizar el descuento.";
			}
		}

		request.setAttribute("message", error == null ? "Descuento actualizado correctamente." : error);
		AdminDiscounts.forwardWithDiscounts(request, response);
	}

}

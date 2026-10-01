package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.entities.User;
import main.java.logic.ProductTypeCRUD;

/**
 * Servlet implementation class ProductTypeCreate
 */
@WebServlet({ "/ProductTypeCreate", "/producttypecreate", "/productTypeCreate", "/Producttypecreate", "/PRODUCTTYPECREATE" })
public class ProductTypeCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductTypeCreate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("AdminProductTypes");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// valida y crea un tipo de producto nuevo desde el modal "Crear tipo" del listado.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null || !u.getRole().equalsIgnoreCase("admin")) {
			response.sendRedirect("index.html");
			return;
		}

		String error;
		try {
			error = new ProductTypeCRUD().addProductType(request.getParameter("name"));
		} catch (SQLException e) {
			e.printStackTrace();
			error = "No se pudo crear el tipo de producto.";
		}

		request.setAttribute("message", error == null ? "Tipo de producto creado correctamente." : error);
		AdminProductTypes.forwardWithProductTypes(request, response);
	}

}

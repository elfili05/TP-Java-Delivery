package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.data.ProductRepository;
import main.java.entities.Product;
import main.java.entities.Restaurant;
import main.java.logic.RestaurantCRUD;

/**
 * Servlet implementation class RestaurantMenu
 */
@WebServlet({ "/RestaurantMenu", "/restaurantmenu", "/restaurantMenu", "/Restaurantmenu", "/RESTAURANTMENU" })
public class RestaurantMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestaurantMenu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// sin sesión iniciada no hay menú que mostrar.
		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}

		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		Restaurant res = new Restaurant();
		Integer res_id = null; 
		
		if (request.getParameter("selectedRestaurant") != null) {
			try {
				res_id = Integer.parseInt(request.getParameter("selectedRestaurant"));
			} catch (NumberFormatException e) {
				// un id que no es número (URL modificada a mano) se trata como un restaurante inexistente.
				response.sendRedirect("MainHome");
				return;
			}
			res.setRestaurant_id(res_id);
			
			try {
				res = ctrlRestaurant.getRestaurant(res);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			res = ((Restaurant)request.getSession().getAttribute("currentRestaurant"));
		}
		
		
		// restaurante inexistente o sin restaurante elegido todavía: se vuelve a la lista.
		if (res == null) {
			response.sendRedirect("MainHome");
			return;
		}

		try {
			if (ctrlRestaurant.isAvailable(res) == false) {
				// no sigue ni guarda este restaurante en la sesión: el pedido no puede armarse sobre uno cerrado.
				request.getRequestDispatcher("WEB-INF/restaurant_unavailable.jsp").forward(request, response);
				return;
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// los tipos del desplegable salen de todos los productos del restaurante (no de la lista filtrada), para poder cambiar de filtro directamente.
		try {
			java.util.LinkedHashSet<String> menuTypes = new java.util.LinkedHashSet<String>();
			for (Product product : new ProductRepository().getAll(res)) {
				if (product.getProduct_type() != null && product.getProduct_type().getName() != null) {
					menuTypes.add(product.getProduct_type().getName());
				}
			}
			request.getSession().setAttribute("menuTypes", menuTypes);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (request.getParameter("productTypeFilter") != null) {
			String product_type_name = request.getParameter("productTypeFilter");
			try {
				request.getSession().setAttribute("products", new ProductRepository().getByType(res, product_type_name));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
		try {
			request.getSession().setAttribute("products", new ProductRepository().getAll(res));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
		//System.out.println(response.getStatus());
		request.getSession().setAttribute("currentRestaurant", res);
		request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

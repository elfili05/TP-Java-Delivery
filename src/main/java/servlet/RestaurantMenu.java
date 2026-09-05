package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.data.ProductRepository;
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
		
		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		Restaurant res = new Restaurant();
		Integer res_id = null; 
		
		if (request.getParameter("selectedRestaurant") != null) {
			res_id = Integer.parseInt(request.getParameter("selectedRestaurant"));
		}
		else {
			res_id = ((Restaurant)request.getSession().getAttribute("currentRestaurant")).getRestaurant_id();
		}
		
		res.setRestaurant_id(res_id);
		
		try {
			res = ctrlRestaurant.getRestaurant(res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (request.getParameter("productTypeFilter") != null) {
			String product_type_name = request.getParameter("productTypeFilter");
			try {
				request.setAttribute("products", new ProductRepository().getByType(res, product_type_name));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
		try {
			request.setAttribute("products", new ProductRepository().getAll(res));
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

package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.logic.ProcessOrder;
import main.java.logic.RestaurantCRUD;
import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.entities.Order;
import main.java.entities.OrderDetail;
import main.java.entities.Product;

import java.util.LinkedList;

/**
 * Servlet implementation class Order
 */
@WebServlet({ "/OrderProcess", "/orderProcess", "/ORDERPROCESS", "/Orderprocess","/orderprocess" })
public class OrderProcess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderProcess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*acá proceso la decisión de la ventana modal: cancelar o confirmar, si confirma, proOrder.addOrder(order), si cancela, vuelvo al menú
		 * de restaurant_menu.jsp
		*/
		ProcessOrder proOrder = new ProcessOrder();
		RestaurantCRUD ctrlRestaurant = new RestaurantCRUD();
		Restaurant currentRes = (Restaurant) request.getSession().getAttribute("currentRestaurant");
		
		if (request.getParameter("confirmOrder") != null) {
			if ((request.getParameter("confirmOrder").equalsIgnoreCase("true"))) {
				try {
					if (ctrlRestaurant.isAvailable(currentRes)) {
						proOrder.addOrder((Order) request.getSession().getAttribute("order"));
						request.getRequestDispatcher("WEB-INF/order_confirmation.jsp").forward(request, response); // order successfull
					} 
					
					else { // redirecting because restaurant is not available upon order confirmation
						
						request.getSession().removeAttribute("order");
						request.removeAttribute("confirmOrder");
						request.getSession().removeAttribute("currentRestaurant");
						request.getSession().removeAttribute("products");
						
						request.getRequestDispatcher("WEB-INF/restaurant_unavailable.jsp").forward(request, response); // order failed
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else { // redirecting because of order cancellation
				request.getSession().removeAttribute("order");
				request.removeAttribute("confirmOrder");
				request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response); // order failed
				
			}
			
			
		}
		else { // redirecting because of empty order
			request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response); // order failed
		}
		
		//request.getRequestDispatcher("WEB-INF/main_page.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// If the user has selected products and quantities, prepare the order and redirect to confirmation modal. Otherwise, redirect back to menu.
	
		ProcessOrder proOrder = new ProcessOrder();
		
		LinkedList<Product> products = (LinkedList<Product>)request.getSession().getAttribute("products");
		Restaurant res = (Restaurant)request.getSession().getAttribute("currentRestaurant");
		User u = (User)request.getSession().getAttribute("user");
		LinkedList<OrderDetail> orderDetails = new LinkedList<OrderDetail>();
		
		
		// create every order detail
		int detail_number = 1;
		for (Product product : products) {
			String quantityStr = request.getParameter("quantity_" + product.getProduct_id());
			if (quantityStr != null) {
				int quantity = Integer.parseInt(quantityStr);
				if (quantity > 0) {
					orderDetails.add(new OrderDetail(product, quantity, detail_number));
					detail_number++;
				}
			}
		}
			
		Order order = null;
		try {
			order = proOrder.prepareOrder(u, res, orderDetails);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getSession().setAttribute("order", order);
		
		if (order == null) { // no products were selected, empty order, redirecting back to menu
			
			doGet(request, response);
		} 
		
		else { // order has items, proceed to confirmation modal.
			request.setAttribute("confirmOrder",true);
			request.getRequestDispatcher("WEB-INF/restaurant_menu.jsp").forward(request, response);
		}
		
		
		
				
		}
		
		
		
	}

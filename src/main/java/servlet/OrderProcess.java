package main.java.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.logic.ProcessOrder;
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProcessOrder proOrder = new ProcessOrder();
		
		LinkedList<Product> products = (LinkedList<Product>)request.getSession().getAttribute("products");
		Restaurant res = (Restaurant)request.getSession().getAttribute("currentRestaurant");
		User u = (User)request.getSession().getAttribute("user");
		LinkedList<OrderDetail> orderDetails = new LinkedList<OrderDetail>();
		
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
			
		Order order = proOrder.prepareOrder(u, res, orderDetails);
		
		if (order == null) {
			//TODO: Handle the case where no products were selected
		} 
		
		else {
			try {
				proOrder.addOrder(order);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
				
		}
		
		
		
	}

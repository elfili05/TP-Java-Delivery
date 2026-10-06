package main.java.logic;

import java.sql.SQLException;
import java.util.LinkedList;
import main.java.data.DiscountRepository;
import main.java.data.OrderRepository;
import main.java.entities.OrderDetail;
import main.java.entities.Restaurant;
import main.java.entities.User;
import main.java.entities.Discount;
import main.java.entities.Order;

public class ProcessOrder {
	private OrderRepository or;
	private DiscountRepository dr;
	
	public ProcessOrder() {
		or = new OrderRepository();
		dr = new DiscountRepository();
	}
	
	
	public Order prepareOrder(User u, Restaurant res, LinkedList<OrderDetail> orderDetails) throws SQLException {

		Order order = new Order(u, res); 
		
		order.setOrder_details(orderDetails);
		
		// el total es una suma de doubles (ej.: 29999.999999999996): se redondea a centavos para que el umbral del descuento se compare bien.
		Discount d = dr.getOne(Math.round(order.getTotal() * 100) / 100.0); 
		
		order.setDiscount(d);
		
		if (!orderDetails.isEmpty()) {
			return order;
		}
		
		else {
			return null;
		}
		
	}
	
	public Boolean addOrder(Order order) throws SQLException {
		return or.addOrder(order);
		
	}
}

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
		double totalAmount = 0;
		for (OrderDetail od : orderDetails) {
			totalAmount += od.getSubtotal();
		}
		
		Discount d = dr.getOne(totalAmount); 
		
		Order order = new Order(u, res, d); 
		if (!orderDetails.isEmpty()) {
			order.setOrder_details(orderDetails);
			return order;
		}
		return null;
	}
	
	public void addOrder(Order order) throws SQLException {
		or.addOrder(order);
		
	}
}

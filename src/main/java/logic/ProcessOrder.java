package main.java.logic;

import java.sql.SQLException;
import java.util.LinkedList;

import main.java.data.OrderRepository;

import main.java.entities.OrderDetail;

import main.java.entities.Restaurant;

import main.java.entities.User;

import main.java.entities.Order;

public class ProcessOrder {
	private OrderRepository or;
	
	public ProcessOrder() {
		or = new OrderRepository();
	}
	
	
	public Order prepareOrder(User u, Restaurant res, LinkedList<OrderDetail> orderDetails) {
		Order order = new Order(u, res);
		if (!orderDetails.isEmpty()) {
			order.setOrder_details(orderDetails);
			return order;
		}
		return null;
	}
	
	public void addOrder(Order order) throws SQLException {
		or.addOrder(order.getOrder_details(), order.getUser(), order.getRestaurant(), order.getTotal());
		
	}
}

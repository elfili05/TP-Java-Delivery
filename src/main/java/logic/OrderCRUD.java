package main.java.logic;

import java.sql.SQLException;
import java.util.LinkedList;

import main.java.data.OrderRepository;
import main.java.entities.Order;

public class OrderCRUD {

	private OrderRepository or;

	public OrderCRUD() {
		or = new OrderRepository();
	}

	public LinkedList<Order> getOrdersByStatus(String status) throws SQLException {
		return or.getByStatus(status);
	}

	public Boolean deliverOrder(int orderId) throws SQLException {
		return or.deliverOrder(orderId);
	}

}

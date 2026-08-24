package java.entities;

import java.time.*;
import java.util.LinkedList;

public class Order {
	private int order_id;
	private LocalDate order_date;
	private User user;
	private Restaurant restaurant;
	private LinkedList<OrderDetail> order_details;
	int getOrder_id() {
		return order_id;
	}
	void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	LocalDate getOrder_date() {
		return order_date;
	}
	void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}
	User getUser() {
		return user;
	}
	void setUser(User user) {
		this.user = user;
	}
	Restaurant getRestaurant() {
		return restaurant;
	}
	void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	LinkedList<OrderDetail> getOrder_details() {
		return order_details;
	}
	void setOrder_details(LinkedList<OrderDetail> order_details) {
		this.order_details = order_details;
	}
	
	double getTotal() {
		double total = 0;
		for (OrderDetail order_detail : order_details) {
			total += order_detail.getSubtotal();
		}
		return total;
	}
	
	
}


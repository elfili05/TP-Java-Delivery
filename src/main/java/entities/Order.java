package main.java.entities;

import java.time.*;
import java.util.LinkedList;

public class Order {
	private int order_id;
	private LocalDate order_date;
	private User user;
	private Restaurant restaurant;
	private LinkedList<OrderDetail> order_details;
	
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public LocalDate getOrder_date() {
		return order_date;
	}
	public void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public LinkedList<OrderDetail> getOrder_details() {
		return order_details;
	}
	public void setOrder_details(LinkedList<OrderDetail> order_details) {
		this.order_details = order_details;
	}
	
	public double getTotal() {
		double total = 0;
		for (OrderDetail order_detail : order_details) {
			total += order_detail.getSubtotal();
		}
		return total;
	}
	
	
}

